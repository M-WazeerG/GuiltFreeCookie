package za.ac.cput.guiltfreecookie.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import za.ac.cput.guiltfreecookie.domain.Admin;
import za.ac.cput.guiltfreecookie.repository.AdminRepository;

import java.io.IOException;
import java.util.List;

/**
 * Authenticates requests carrying "Authorization: Bearer <jwt>". A valid
 * signature isn't enough on its own: the admin must still exist and be
 * active, and the token must carry their current token version (bumped on
 * every password change or reset), so deactivating an admin or changing
 * their password ends every session they had straight away.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final AdminRepository adminRepository;

    public JwtAuthenticationFilter(JwtService jwtService, AdminRepository adminRepository) {
        this.jwtService = jwtService;
        this.adminRepository = adminRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith(BEARER_PREFIX)) {
            jwtService.parse(header.substring(BEARER_PREFIX.length()))
                    .flatMap(claims -> adminRepository.findById(claims.getSubject())
                            .filter(admin -> isStillValid(admin, claims)))
                    .ifPresent(admin -> {
                        var authentication = new UsernamePasswordAuthenticationToken(
                                admin.getAdminId(), null, List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    });
        }

        filterChain.doFilter(request, response);
    }

    static boolean isStillValid(Admin admin, Claims claims) {
        // Tokens from before versioning carry no claim: they count as version 0.
        Object version = claims.get(JwtService.VERSION_CLAIM);
        int tokenVersion = version instanceof Number number ? number.intValue() : 0;
        return admin.isActive() && tokenVersion == admin.getTokenVersion();
    }
}

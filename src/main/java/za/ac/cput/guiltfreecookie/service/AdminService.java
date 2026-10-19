package za.ac.cput.guiltfreecookie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import za.ac.cput.guiltfreecookie.domain.Admin;
import za.ac.cput.guiltfreecookie.repository.AdminRepository;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class AdminService implements IService<Admin, String> {

    private static final String TEMP_PASSWORD_CHARS =
            "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789";
    private static final int TEMP_PASSWORD_LENGTH = 10;
    private static final long TEMP_PASSWORD_VALID_HOURS = 24;
    private static final Pattern BCRYPT_HASH_PATTERN = Pattern.compile("^\\$2[aby]\\$");

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom random = new SecureRandom();
    private volatile String dummyHash;

    @Autowired
    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Admin create(Admin admin) {
        Admin toSave = new Admin.Builder()
                .copy(admin)
                .setPassword(passwordEncoder.encode(admin.getPassword()))
                .build();
        return adminRepository.save(toSave);
    }

    @Override
    public Admin read(String id) {
        return adminRepository.findById(id).orElse(null);
    }

    @Override
    public Admin update(Admin admin) {
        Admin existing = adminRepository.findById(admin.getAdminId()).orElse(null);

        if (existing == null) {
            return null;
        }

        Admin updated = new Admin.Builder()
                .copy(existing)
                .setFirstName(admin.getFirstName())
                .setLastName(admin.getLastName())
                .setEmail(admin.getEmail())
                .build();

        return adminRepository.save(updated);
    }

    @Override
    public boolean delete(String id) {
        if (adminRepository.existsById(id)) {
            adminRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Admin> getAll() {
        return adminRepository.findAll();
    }

    public Admin login(String email, String password) {
        if (email == null || password == null) {
            return null;
        }

        Admin admin = adminRepository.findByEmail(email).orElse(null);

        // Always run one bcrypt check, even for an unknown email, so response
        // time doesn't reveal which emails belong to an admin.
        String hash = admin != null ? admin.getPassword() : dummyHash();
        boolean passwordMatches = hash != null && passwordEncoder.matches(password, hash);

        if (admin == null || !admin.isActive() || !passwordMatches) {
            return null;
        }

        if (admin.getPasswordExpiresAt() != null
                && LocalDateTime.now().isAfter(admin.getPasswordExpiresAt())) {
            return null;
        }

        return admin;
    }

    /** True if another admin (not {@code exceptAdminId}) already uses this email. */
    public boolean isEmailTaken(String email, String exceptAdminId) {
        return adminRepository.findByEmail(email)
                .filter(other -> !other.getAdminId().equals(exceptAdminId))
                .isPresent();
    }

    public Admin setActive(String id, boolean active) {
        Admin existing = adminRepository.findById(id).orElse(null);

        if (existing == null) {
            return null;
        }

        Admin updated = new Admin.Builder()
                .copy(existing)
                .setActive(active)
                .build();

        return adminRepository.save(updated);
    }

    public PasswordResetResult resetPassword(String id) {
        Admin existing = adminRepository.findById(id).orElse(null);

        if (existing == null) {
            return null;
        }

        String temporaryPassword = generateTempPassword();
        Admin updated = new Admin.Builder()
                .copy(existing)
                .setPassword(passwordEncoder.encode(temporaryPassword))
                .setPasswordExpiresAt(LocalDateTime.now().plusHours(TEMP_PASSWORD_VALID_HOURS))
                .setTokenVersion(existing.getTokenVersion() + 1)
                .build();

        Admin saved = adminRepository.save(updated);
        return new PasswordResetResult(saved, temporaryPassword);
    }

    public Admin changePassword(String id, String newPassword) {
        Admin existing = adminRepository.findById(id).orElse(null);

        if (existing == null) {
            return null;
        }

        Admin updated = new Admin.Builder()
                .copy(existing)
                .setPassword(passwordEncoder.encode(newPassword))
                .setPasswordExpiresAt(null)
                .setTokenVersion(existing.getTokenVersion() + 1)
                .build();

        return adminRepository.save(updated);
    }

    // Runs once at startup so admins created before password hashing was added
    // (plain text in the database) can still log in — their existing password
    // keeps working, only its stored form changes from plain text to a hash.
    @EventListener(ApplicationReadyEvent.class)
    public void migratePlaintextPasswords() {
        for (Admin admin : adminRepository.findAll()) {
            if (admin.getPassword() != null && !BCRYPT_HASH_PATTERN.matcher(admin.getPassword()).lookingAt()) {
                Admin rehashed = new Admin.Builder()
                        .copy(admin)
                        .setPassword(passwordEncoder.encode(admin.getPassword()))
                        .build();
                adminRepository.save(rehashed);
            }
        }
    }

    private String dummyHash() {
        if (dummyHash == null) {
            dummyHash = passwordEncoder.encode(UUID.randomUUID().toString());
        }
        return dummyHash;
    }

    private String generateTempPassword() {
        StringBuilder builder = new StringBuilder(TEMP_PASSWORD_LENGTH);
        for (int i = 0; i < TEMP_PASSWORD_LENGTH; i++) {
            builder.append(TEMP_PASSWORD_CHARS.charAt(random.nextInt(TEMP_PASSWORD_CHARS.length())));
        }
        return builder.toString();
    }
}

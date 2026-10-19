package za.ac.cput.guiltfreecookie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import za.ac.cput.guiltfreecookie.domain.Admin;

import za.ac.cput.guiltfreecookie.security.JwtService;
import za.ac.cput.guiltfreecookie.service.AdminService;
import za.ac.cput.guiltfreecookie.service.PasswordResetResult;
import za.ac.cput.guiltfreecookie.util.Helper;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    static final int MAX_NAME_LENGTH = 100;

    private final AdminService adminService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(AdminService adminService, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.adminService = adminService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Admin admin) {
        // Built from the allowed fields only, so a request can't choose the
        // new admin's id (and overwrite someone) or pre-set its flags.
        Admin toCreate = new Admin.Builder()
                .setFirstName(Helper.clean(admin.getFirstName()))
                .setLastName(Helper.clean(admin.getLastName()))
                .setEmail(Helper.cleanEmail(admin.getEmail()))
                .setPassword(admin.getPassword())
                .build();

        if (!hasValidProfile(toCreate) || !Helper.isValidPassword(toCreate.getPassword())) {
            return ResponseEntity.badRequest().build();
        }
        if (adminService.isEmailTaken(toCreate.getEmail(), null)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("An admin with that email already exists");
        }

        return new ResponseEntity<>(adminService.create(toCreate), HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Admin>> getAll() {
        return ResponseEntity.ok(adminService.getAll());
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Admin> read(@PathVariable String id) {
        Admin admin = adminService.read(id);

        if (admin == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(admin);
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody Admin admin) {
        Admin toUpdate = new Admin.Builder()
                .setAdminId(admin.getAdminId())
                .setFirstName(Helper.clean(admin.getFirstName()))
                .setLastName(Helper.clean(admin.getLastName()))
                .setEmail(Helper.cleanEmail(admin.getEmail()))
                .build();

        if (Helper.isNullOrEmpty(toUpdate.getAdminId()) || !hasValidProfile(toUpdate)) {
            return ResponseEntity.badRequest().build();
        }
        if (adminService.isEmailTaken(toUpdate.getEmail(), toUpdate.getAdminId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("An admin with that email already exists");
        }

        Admin updated = adminService.update(toUpdate);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id, @AuthenticationPrincipal String currentAdminId) {
        if (id.equals(currentAdminId)) {
            return ResponseEntity.badRequest().body("You can't delete your own account");
        }
        adminService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Admin admin) {

        Admin foundAdmin = adminService.login(Helper.cleanEmail(admin.getEmail()), admin.getPassword());

        if (foundAdmin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
        }

        String token = jwtService.generateToken(foundAdmin);
        return ResponseEntity.ok(new LoginResponse(foundAdmin, token));
    }

    @PutMapping("/active/{id}")
    public ResponseEntity<?> setActive(
            @PathVariable String id,
            @RequestParam boolean active,
            @AuthenticationPrincipal String currentAdminId) {
        if (!active && id.equals(currentAdminId)) {
            return ResponseEntity.badRequest().body("You can't deactivate your own account");
        }

        Admin updated = adminService.setActive(id, active);

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updated);
    }

    @PostMapping("/resetPassword/{id}")
    public ResponseEntity<PasswordResetResult> resetPassword(@PathVariable String id) {
        PasswordResetResult result = adminService.resetPassword(id);

        if (result == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }

    // Changing a password ends every other session (see
    // JwtAuthenticationFilter), so the caller gets a fresh token back.
    @PutMapping("/changePassword/{id}")
    public ResponseEntity<?> changePassword(
            @PathVariable String id,
            @RequestBody ChangePasswordRequest request,
            @AuthenticationPrincipal String currentAdminId) {

        if (!id.equals(currentAdminId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You can only change your own password");
        }

        Admin admin = adminService.read(id);
        if (admin == null) {
            return ResponseEntity.notFound().build();
        }

        if (request.getCurrentPassword() == null
                || !passwordEncoder.matches(request.getCurrentPassword(), admin.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Current password is incorrect");
        }

        if (!Helper.isValidPassword(request.getNewPassword())) {
            return ResponseEntity.badRequest().body(
                    "New password must be at least " + Helper.MIN_PASSWORD_LENGTH + " characters");
        }

        Admin updated = adminService.changePassword(id, request.getNewPassword());
        String token = jwtService.generateToken(updated);
        return ResponseEntity.ok(new LoginResponse(updated, token));
    }

    private static boolean hasValidProfile(Admin admin) {
        return Helper.isPresent(admin.getFirstName(), MAX_NAME_LENGTH)
                && Helper.isPresent(admin.getLastName(), MAX_NAME_LENGTH)
                && Helper.isValidEmail(admin.getEmail());
    }
}

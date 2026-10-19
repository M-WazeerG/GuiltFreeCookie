package za.ac.cput.guiltfreecookie.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import za.ac.cput.guiltfreecookie.domain.Admin;
import za.ac.cput.guiltfreecookie.security.JwtService;
import za.ac.cput.guiltfreecookie.service.AdminService;
import za.ac.cput.guiltfreecookie.service.PasswordResetResult;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminController adminController;

    private Admin existingAdmin;

    @BeforeEach
    void setUp() {
        existingAdmin = new Admin.Builder()
                .setAdminId("AD001")
                .setFirstName("Jane")
                .setLastName("Doe")
                .setEmail("jane.doe@guiltfreecookie.com")
                .setPassword("$2a$10$hashedSecurePass123")
                .setActive(true)
                .build();
    }

    private ChangePasswordRequest changePasswordRequest(String currentPassword, String newPassword) {
        ChangePasswordRequest request = new ChangePasswordRequest();
        ReflectionTestUtils.setField(request, "currentPassword", currentPassword);
        ReflectionTestUtils.setField(request, "newPassword", newPassword);
        return request;
    }

    private Admin newAdminRequest() {
        return new Admin.Builder()
                .setFirstName("Sam")
                .setLastName("Mokoena")
                .setEmail("sam@guiltfreecookie.com")
                .setPassword("SecurePass123")
                .build();
    }

    @Test
    void testCreate() {
        when(adminService.isEmailTaken("sam@guiltfreecookie.com", null)).thenReturn(false);
        when(adminService.create(any(Admin.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<?> result = adminController.create(newAdminRequest());

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("sam@guiltfreecookie.com", ((Admin) result.getBody()).getEmail());
    }

    @Test
    void testCreateIgnoresClientChosenIdAndFlags() {
        Admin sneaky = new Admin.Builder()
                .copy(newAdminRequest())
                .setAdminId("AD001")
                .setActive(false)
                .setPasswordExpiresAt(LocalDateTime.now().plusYears(10))
                .build();
        when(adminService.isEmailTaken(anyString(), any())).thenReturn(false);
        ArgumentCaptor<Admin> saved = ArgumentCaptor.forClass(Admin.class);
        when(adminService.create(saved.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        adminController.create(sneaky);

        assertNull(saved.getValue().getAdminId(), "the database must assign the id");
        assertTrue(saved.getValue().isActive());
        assertNull(saved.getValue().getPasswordExpiresAt());
    }

    @Test
    void testCreateNormalisesEmail() {
        Admin request = new Admin.Builder().copy(newAdminRequest()).setEmail("  Sam@GuiltFreeCookie.com ").build();
        when(adminService.isEmailTaken("sam@guiltfreecookie.com", null)).thenReturn(false);
        when(adminService.create(any(Admin.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<?> result = adminController.create(request);

        assertEquals("sam@guiltfreecookie.com", ((Admin) result.getBody()).getEmail());
    }

    @Test
    void testCreateWithDuplicateEmailReturnsConflict() {
        when(adminService.isEmailTaken("sam@guiltfreecookie.com", null)).thenReturn(true);

        ResponseEntity<?> result = adminController.create(newAdminRequest());

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        verify(adminService, never()).create(any(Admin.class));
    }

    @Test
    void testCreateWithShortPasswordReturnsBadRequest() {
        Admin weak = new Admin.Builder().copy(newAdminRequest()).setPassword("short").build();

        ResponseEntity<?> result = adminController.create(weak);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verify(adminService, never()).create(any(Admin.class));
    }

    @Test
    void testCreateWithPasswordOverBcryptLimitReturnsBadRequest() {
        Admin tooLong = new Admin.Builder().copy(newAdminRequest()).setPassword("a".repeat(73)).build();

        ResponseEntity<?> result = adminController.create(tooLong);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verify(adminService, never()).create(any(Admin.class));
    }

    @Test
    void testCreateWithInvalidEmailReturnsBadRequestAndNeverPersists() {
        Admin invalid = new Admin.Builder().copy(newAdminRequest()).setEmail("not-an-email").build();

        ResponseEntity<?> result = adminController.create(invalid);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verify(adminService, never()).create(any(Admin.class));
    }

    @Test
    void testCreateWithMissingFirstNameReturnsBadRequestAndNeverPersists() {
        Admin invalid = new Admin.Builder().copy(newAdminRequest()).setFirstName("   ").build();

        ResponseEntity<?> result = adminController.create(invalid);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verify(adminService, never()).create(any(Admin.class));
    }

    @Test
    void testGetAll() {
        when(adminService.getAll()).thenReturn(List.of(existingAdmin));

        ResponseEntity<List<Admin>> result = adminController.getAll();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
    }

    @Test
    void testReadFound() {
        when(adminService.read("AD001")).thenReturn(existingAdmin);

        ResponseEntity<Admin> result = adminController.read("AD001");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("jane.doe@guiltfreecookie.com", result.getBody().getEmail());
    }

    @Test
    void testReadNotFound() {
        when(adminService.read("missing")).thenReturn(null);

        ResponseEntity<Admin> result = adminController.read("missing");

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testUpdate() {
        Admin updated = new Admin.Builder().copy(existingAdmin).setFirstName("Janet").build();
        when(adminService.isEmailTaken("jane.doe@guiltfreecookie.com", "AD001")).thenReturn(false);
        when(adminService.update(any(Admin.class))).thenReturn(updated);

        ResponseEntity<?> result = adminController.update(existingAdmin);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Janet", ((Admin) result.getBody()).getFirstName());
    }

    @Test
    void testUpdateToAnotherAdminsEmailReturnsConflict() {
        when(adminService.isEmailTaken("jane.doe@guiltfreecookie.com", "AD001")).thenReturn(true);

        ResponseEntity<?> result = adminController.update(existingAdmin);

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        verify(adminService, never()).update(any(Admin.class));
    }

    @Test
    void testUpdateWithInvalidEmailReturnsBadRequestAndNeverPersists() {
        Admin invalid = new Admin.Builder().copy(existingAdmin).setEmail("not-an-email").build();

        ResponseEntity<?> result = adminController.update(invalid);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verify(adminService, never()).update(any(Admin.class));
    }

    @Test
    void testUpdateWhenServiceReturnsNullReturnsNotFound() {
        when(adminService.isEmailTaken(anyString(), anyString())).thenReturn(false);
        when(adminService.update(any(Admin.class))).thenReturn(null);

        ResponseEntity<?> result = adminController.update(existingAdmin);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testDelete() {
        ResponseEntity<?> result = adminController.delete("AD002", "AD001");

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(adminService).delete("AD002");
    }

    @Test
    void testDeleteYourselfIsRefused() {
        ResponseEntity<?> result = adminController.delete("AD001", "AD001");

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verify(adminService, never()).delete(anyString());
    }

    @Test
    void testLoginSuccessReturnsAdminAndToken() {
        Admin credentials = new Admin.Builder()
                .setEmail("Jane.Doe@guiltfreecookie.com")
                .setPassword("SecurePass123")
                .build();
        when(adminService.login("jane.doe@guiltfreecookie.com", "SecurePass123")).thenReturn(existingAdmin);
        when(jwtService.generateToken(existingAdmin)).thenReturn("fake.jwt.token");

        ResponseEntity<?> result = adminController.login(credentials);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        LoginResponse body = (LoginResponse) result.getBody();
        assertEquals(existingAdmin, body.getAdmin());
        assertEquals("fake.jwt.token", body.getToken());
    }

    @Test
    void testLoginFailureReturnsUnauthorized() {
        Admin credentials = new Admin.Builder()
                .setEmail("jane.doe@guiltfreecookie.com")
                .setPassword("WrongPassword")
                .build();
        when(adminService.login("jane.doe@guiltfreecookie.com", "WrongPassword")).thenReturn(null);

        ResponseEntity<?> result = adminController.login(credentials);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Invalid email or password", result.getBody());
    }

    @Test
    void testSetActiveFound() {
        Admin deactivated = new Admin.Builder().copy(existingAdmin).setActive(false).build();
        when(adminService.setActive("AD001", false)).thenReturn(deactivated);

        ResponseEntity<?> result = adminController.setActive("AD001", false, "AD002");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertFalse(((Admin) result.getBody()).isActive());
    }

    @Test
    void testDeactivateYourselfIsRefused() {
        ResponseEntity<?> result = adminController.setActive("AD001", false, "AD001");

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verify(adminService, never()).setActive(anyString(), any(Boolean.class));
    }

    @Test
    void testSetActiveNotFound() {
        when(adminService.setActive("missing", true)).thenReturn(null);

        ResponseEntity<?> result = adminController.setActive("missing", true, "AD001");

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testResetPasswordFoundReturnsAdminAndTemporaryPassword() {
        Admin reset = new Admin.Builder().copy(existingAdmin).setPassword("hashed-temp").build();
        PasswordResetResult resetResult = new PasswordResetResult(reset, "TempPass1234");
        when(adminService.resetPassword("AD001")).thenReturn(resetResult);

        ResponseEntity<PasswordResetResult> result = adminController.resetPassword("AD001");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("TempPass1234", result.getBody().getTemporaryPassword());
        assertEquals("hashed-temp", result.getBody().getAdmin().getPassword());
    }

    @Test
    void testResetPasswordNotFound() {
        when(adminService.resetPassword("missing")).thenReturn(null);

        ResponseEntity<PasswordResetResult> result = adminController.resetPassword("missing");

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testChangingSomeoneElsesPasswordIsForbidden() {
        ResponseEntity<?> result = adminController.changePassword(
                "AD002", changePasswordRequest("whatever", "NewPassword456"), "AD001");

        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        verify(adminService, never()).changePassword(any(), any());
    }

    @Test
    void testChangePasswordWhenAdminNotFoundReturnsNotFound() {
        when(adminService.read("missing")).thenReturn(null);

        ResponseEntity<?> result = adminController.changePassword(
                "missing", changePasswordRequest("old", "new"), "missing");

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(adminService, never()).changePassword(any(), any());
    }

    @Test
    void testChangePasswordWithWrongCurrentPasswordReturnsUnauthorized() {
        when(adminService.read("AD001")).thenReturn(existingAdmin);
        when(passwordEncoder.matches("WrongCurrent", existingAdmin.getPassword())).thenReturn(false);

        ResponseEntity<?> result = adminController.changePassword(
                "AD001", changePasswordRequest("WrongCurrent", "NewPassword456"), "AD001");

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Current password is incorrect", result.getBody());
        verify(adminService, never()).changePassword(any(), any());
    }

    @Test
    void testChangePasswordWithEmptyNewPasswordReturnsBadRequest() {
        when(adminService.read("AD001")).thenReturn(existingAdmin);
        when(passwordEncoder.matches("SecurePass123", existingAdmin.getPassword())).thenReturn(true);

        ResponseEntity<?> result = adminController.changePassword(
                "AD001", changePasswordRequest("SecurePass123", ""), "AD001");

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verify(adminService, never()).changePassword(any(), any());
    }

    @Test
    void testChangePasswordWithShortNewPasswordReturnsBadRequest() {
        when(adminService.read("AD001")).thenReturn(existingAdmin);
        when(passwordEncoder.matches("SecurePass123", existingAdmin.getPassword())).thenReturn(true);

        ResponseEntity<?> result = adminController.changePassword(
                "AD001", changePasswordRequest("SecurePass123", "short"), "AD001");

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verify(adminService, never()).changePassword(any(), any());
    }

    @Test
    void testChangePasswordWithCorrectCurrentPasswordReturnsFreshToken() {
        Admin changed = new Admin.Builder().copy(existingAdmin).setPassword("hashed-new-password").build();
        when(adminService.read("AD001")).thenReturn(existingAdmin);
        when(passwordEncoder.matches("SecurePass123", existingAdmin.getPassword())).thenReturn(true);
        when(adminService.changePassword("AD001", "NewPassword456")).thenReturn(changed);
        when(jwtService.generateToken(changed)).thenReturn("fresh.jwt.token");

        ResponseEntity<?> result = adminController.changePassword(
                "AD001", changePasswordRequest("SecurePass123", "NewPassword456"), "AD001");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        LoginResponse body = (LoginResponse) result.getBody();
        assertEquals(changed, body.getAdmin());
        assertEquals("fresh.jwt.token", body.getToken());
        verify(adminService).changePassword("AD001", "NewPassword456");
    }
}

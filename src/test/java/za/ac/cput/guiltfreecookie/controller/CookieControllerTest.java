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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import za.ac.cput.guiltfreecookie.domain.Cookie;
import za.ac.cput.guiltfreecookie.domain.CookieCategory;
import za.ac.cput.guiltfreecookie.service.CookieService;
import za.ac.cput.guiltfreecookie.service.ImageStorageService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CookieControllerTest {

    @Mock
    private CookieService cookieService;

    @Mock
    private ImageStorageService imageStorageService;

    @InjectMocks
    private CookieController cookieController;

    private Cookie existingCookie;
    private MultipartFile image;

    @BeforeEach
    void setUp() {
        existingCookie = new Cookie.Builder()
                .setCookieId("CK099")
                .setCategory(CookieCategory.GLUTEN_FREE)
                .setDescription("Gluten Free Coconut Cookie")
                .setIngredients("Coconut flour, Desiccated coconut, Butter")
                .setAllergies("Dairy, Eggs")
                .setPrice(28.50)
                .setImage("cookies/existing.jpg")
                .build();

        image = new MockMultipartFile("image", "cookie.jpg", "image/jpeg", "fake-image-bytes".getBytes());
    }

    @Test
    void testGetAllDelegatesToActiveOnlyList() {
        when(cookieService.getAllActive()).thenReturn(List.of(existingCookie));

        ResponseEntity<List<Cookie>> result = cookieController.getAll();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        verify(cookieService).getAllActive();
        verify(cookieService, never()).getAll();
    }

    @Test
    void testGetAllForAdminDelegatesToFullList() {
        Cookie archived = new Cookie.Builder().copy(existingCookie).setArchived(true).build();
        when(cookieService.getAll()).thenReturn(List.of(existingCookie, archived));

        ResponseEntity<List<Cookie>> result = cookieController.getAllForAdmin();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().size());
        verify(cookieService).getAll();
    }

    @Test
    void testCreateSuccessStoresImageAndPersistsCookie() {
        when(imageStorageService.store(image)).thenReturn("cookies/new.jpg");
        when(cookieService.create(any(Cookie.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<?> result = cookieController.create(existingCookie, image);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("cookies/new.jpg", ((Cookie) result.getBody()).getImage());
        verify(cookieService).create(any(Cookie.class));
    }

    @Test
    void testCreateLetsTheDatabaseAssignTheId() {
        when(imageStorageService.store(image)).thenReturn("cookies/new.jpg");
        ArgumentCaptor<Cookie> saved = ArgumentCaptor.forClass(Cookie.class);
        when(cookieService.create(saved.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        cookieController.create(existingCookie, image);

        assertNull(saved.getValue().getCookieId(), "a client-supplied id could overwrite another cookie");
    }

    @Test
    void testCreateTrimsTextFields() {
        Cookie padded = new Cookie.Builder().copy(existingCookie).setDescription("  Coconut Cookie \u0007").build();
        when(imageStorageService.store(image)).thenReturn("cookies/new.jpg");
        when(cookieService.create(any(Cookie.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<?> result = cookieController.create(padded, image);

        assertEquals("Coconut Cookie", ((Cookie) result.getBody()).getDescription());
    }

    @Test
    void testCreateWithoutCategoryReturnsBadRequest() {
        Cookie invalid = new Cookie.Builder().copy(existingCookie).setCategory(null).build();

        ResponseEntity<?> result = cookieController.create(invalid, image);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verify(cookieService, never()).create(any(Cookie.class));
    }

    @Test
    void testCreateWithOverlongDescriptionReturnsBadRequest() {
        Cookie invalid = new Cookie.Builder().copy(existingCookie).setDescription("x".repeat(256)).build();

        ResponseEntity<?> result = cookieController.create(invalid, image);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verify(cookieService, never()).create(any(Cookie.class));
    }

    @Test
    void testCreateWithAbsurdPriceReturnsBadRequest() {
        Cookie invalid = new Cookie.Builder().copy(existingCookie).setPrice(1_000_000.0).build();

        ResponseEntity<?> result = cookieController.create(invalid, image);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verify(cookieService, never()).create(any(Cookie.class));
    }

    @Test
    void testCreateWithMissingDescriptionReturnsBadRequestAndNeverPersists() {
        Cookie invalid = new Cookie.Builder().copy(existingCookie).setDescription("").build();

        ResponseEntity<?> result = cookieController.create(invalid, image);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verify(imageStorageService, never()).store(any());
        verify(cookieService, never()).create(any(Cookie.class));
    }

    @Test
    void testCreateWithNonPositivePriceReturnsBadRequestAndNeverPersists() {
        Cookie invalid = new Cookie.Builder().copy(existingCookie).setPrice(0.0).build();

        ResponseEntity<?> result = cookieController.create(invalid, image);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verify(imageStorageService, never()).store(any());
        verify(cookieService, never()).create(any(Cookie.class));
    }

    @Test
    void testCreateWithInvalidImageReturnsBadRequestAndNeverPersists() {
        when(imageStorageService.store(image)).thenThrow(new IllegalArgumentException("Uploaded file must be an image"));

        ResponseEntity<?> result = cookieController.create(existingCookie, image);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verify(cookieService, never()).create(any(Cookie.class));
    }

    @Test
    void testReadFound() {
        when(cookieService.read("CK099")).thenReturn(existingCookie);

        ResponseEntity<Cookie> result = cookieController.read("CK099");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Gluten Free Coconut Cookie", result.getBody().getDescription());
    }

    @Test
    void testReadArchivedCookieIsNotFound() {
        Cookie archived = new Cookie.Builder().copy(existingCookie).setArchived(true).build();
        when(cookieService.read("CK099")).thenReturn(archived);

        ResponseEntity<Cookie> result = cookieController.read("CK099");

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testReadNotFound() {
        when(cookieService.read("missing")).thenReturn(null);

        ResponseEntity<Cookie> result = cookieController.read("missing");

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testUpdateWhenCookieMissingReturnsNotFoundAndSkipsImageWork() {
        when(cookieService.read("missing")).thenReturn(null);

        ResponseEntity<?> result = cookieController.update("missing", existingCookie, null);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(imageStorageService, never()).store(any());
        verify(imageStorageService, never()).delete(any());
        verify(cookieService, never()).update(any(Cookie.class));
    }

    @Test
    void testUpdateWithoutNewImageKeepsExistingImage() {
        when(cookieService.read("CK099")).thenReturn(existingCookie);
        when(cookieService.update(any(Cookie.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<?> result = cookieController.update("CK099", existingCookie, null);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("cookies/existing.jpg", ((Cookie) result.getBody()).getImage());
        verify(imageStorageService, never()).store(any());
        verify(imageStorageService, never()).delete(any());
    }

    @Test
    void testUpdateWithInvalidFieldsReturnsBadRequestAndSkipsImageWork() {
        when(cookieService.read("CK099")).thenReturn(existingCookie);
        Cookie invalid = new Cookie.Builder().copy(existingCookie).setAllergies("").build();

        ResponseEntity<?> result = cookieController.update("CK099", invalid, null);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verify(imageStorageService, never()).store(any());
        verify(imageStorageService, never()).delete(any());
        verify(cookieService, never()).update(any(Cookie.class));
    }

    @Test
    void testUpdateWithNewImageReplacesAndDeletesOldOne() {
        when(cookieService.read("CK099")).thenReturn(existingCookie);
        when(imageStorageService.store(image)).thenReturn("cookies/replacement.jpg");
        when(cookieService.update(any(Cookie.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<?> result = cookieController.update("CK099", existingCookie, image);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("cookies/replacement.jpg", ((Cookie) result.getBody()).getImage());
        verify(imageStorageService).delete("cookies/existing.jpg");
    }

    @Test
    void testUpdateWithInvalidNewImageReturnsBadRequestAndKeepsOldImage() {
        when(cookieService.read("CK099")).thenReturn(existingCookie);
        when(imageStorageService.store(image)).thenThrow(new IllegalArgumentException("Uploaded file must be an image"));

        ResponseEntity<?> result = cookieController.update("CK099", existingCookie, image);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verify(imageStorageService, never()).delete(any());
        verify(cookieService, never()).update(any(Cookie.class));
    }

    @Test
    void testUpdateWhenServiceReturnsNullYieldsNotFound() {
        when(cookieService.read("CK099")).thenReturn(existingCookie);
        when(cookieService.update(any(Cookie.class))).thenReturn(null);

        ResponseEntity<?> result = cookieController.update("CK099", existingCookie, null);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testDeleteExistingCookieAlsoDeletesItsImage() {
        when(cookieService.read("CK099")).thenReturn(existingCookie);

        ResponseEntity<Void> result = cookieController.delete("CK099");

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(cookieService).delete("CK099");
        verify(imageStorageService).delete("cookies/existing.jpg");
    }

    @Test
    void testDeleteMissingCookieSkipsImageDelete() {
        when(cookieService.read("missing")).thenReturn(null);

        ResponseEntity<Void> result = cookieController.delete("missing");

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(cookieService).delete("missing");
        verify(imageStorageService, never()).delete(any());
    }

    @Test
    void testArchiveFound() {
        Cookie archived = new Cookie.Builder().copy(existingCookie).setArchived(true).build();
        when(cookieService.setArchived("CK099", true)).thenReturn(archived);

        ResponseEntity<Cookie> result = cookieController.archive("CK099");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isArchived());
    }

    @Test
    void testArchiveNotFound() {
        when(cookieService.setArchived("missing", true)).thenReturn(null);

        ResponseEntity<Cookie> result = cookieController.archive("missing");

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testUnarchiveFound() {
        when(cookieService.setArchived("CK099", false)).thenReturn(existingCookie);

        ResponseEntity<Cookie> result = cookieController.unarchive("CK099");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(false, result.getBody().isArchived());
    }

    @Test
    void testUnarchiveNotFound() {
        when(cookieService.setArchived("missing", false)).thenReturn(null);

        ResponseEntity<Cookie> result = cookieController.unarchive("missing");

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
}

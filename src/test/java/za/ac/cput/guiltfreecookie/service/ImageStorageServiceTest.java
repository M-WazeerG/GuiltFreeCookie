package za.ac.cput.guiltfreecookie.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImageStorageServiceTest {

    private static final byte[] PNG = {(byte) 0x89, 'P', 'N', 'G', 0x0D, 0x0A, 0x1A, 0x0A, 0, 0, 0, 0};
    private static final byte[] JPEG = {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0, 0, 0x10};
    private static final byte[] WEBP = {'R', 'I', 'F', 'F', 0, 0, 0, 0, 'W', 'E', 'B', 'P'};
    private static final byte[] GIF = {'G', 'I', 'F', '8', '9', 'a', 0, 0};

    @TempDir
    Path uploads;

    private ImageStorageService service() {
        return new ImageStorageService(uploads.toString(), "cookies");
    }

    @Test
    void recognisesImagesByTheirBytes() {
        assertEquals(".png", ImageStorageService.extensionFor(PNG));
        assertEquals(".jpg", ImageStorageService.extensionFor(JPEG));
        assertEquals(".webp", ImageStorageService.extensionFor(WEBP));
        assertEquals(".gif", ImageStorageService.extensionFor(GIF));
    }

    @Test
    void namesTheFileFromItsContentNotTheUploadName() {
        MockMultipartFile file = new MockMultipartFile("image", "evil.html", "text/html", PNG);

        String stored = service().store(file);

        assertTrue(stored.startsWith("cookies/") && stored.endsWith(".png"), stored);
        assertTrue(Files.exists(uploads.resolve(stored)));
    }

    @Test
    void rejectsSvgEvenWhenItClaimsToBeAnImage() {
        byte[] svg = "<svg xmlns=\"http://www.w3.org/2000/svg\"><script>alert(1)</script></svg>"
                .getBytes(StandardCharsets.UTF_8);
        MockMultipartFile file = new MockMultipartFile("image", "cookie.svg", "image/svg+xml", svg);

        assertThrows(IllegalArgumentException.class, () -> service().store(file));
    }

    @Test
    void rejectsHtmlRenamedToPng() {
        byte[] html = "<!doctype html><script>alert(1)</script>".getBytes(StandardCharsets.UTF_8);
        MockMultipartFile file = new MockMultipartFile("image", "cookie.png", "image/png", html);

        assertThrows(IllegalArgumentException.class, () -> service().store(file));
    }

    @Test
    void rejectsAnEmptyUpload() {
        MockMultipartFile file = new MockMultipartFile("image", "cookie.png", "image/png", new byte[0]);

        assertThrows(IllegalArgumentException.class, () -> service().store(file));
    }

    @Test
    void neverDeletesOutsideTheUploadFolder() throws Exception {
        // A sibling of the upload folder, reachable with "../" if unchecked.
        Path outside = Files.createFile(uploads.resolveSibling(uploads.getFileName() + "-outside.txt"));
        try {
            service().delete("../" + outside.getFileName());
            service().delete(outside.toAbsolutePath().toString());

            assertTrue(Files.exists(outside));
        } finally {
            Files.deleteIfExists(outside);
        }
    }

    @Test
    void deletesAStoredImage() {
        String stored = service().store(new MockMultipartFile("image", "c.png", "image/png", PNG));

        service().delete(stored);

        assertTrue(Files.notExists(uploads.resolve(stored)));
    }
}

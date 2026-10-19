package za.ac.cput.guiltfreecookie.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.UUID;

@Service
public class ImageStorageService {

    private static final int SIGNATURE_LENGTH = 12;

    private final Path rootDir;
    private final String cookieSubdir;

    public ImageStorageService(
            @Value("${app.upload.root-dir}") String rootDir,
            @Value("${app.upload.cookie-subdir}") String cookieSubdir) {
        this.rootDir = Path.of(rootDir).toAbsolutePath().normalize();
        this.cookieSubdir = cookieSubdir;
    }

    /**
     * Saves an uploaded image under a random name and returns its path
     * relative to the upload root. The type is decided by the file's own
     * bytes, never by the name or Content-Type the client sent, so an HTML or
     * SVG file (which a browser could run as a page) can't be passed off as
     * an image.
     */
    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("An image file is required");
        }

        try {
            String extension = detectExtension(file);
            if (extension == null) {
                throw new IllegalArgumentException("Upload a JPEG, PNG, WebP or GIF image");
            }

            Path targetDir = rootDir.resolve(cookieSubdir);
            Files.createDirectories(targetDir);

            String filename = UUID.randomUUID() + extension;
            file.transferTo(targetDir.resolve(filename));

            return cookieSubdir + "/" + filename;
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to store uploaded image", e);
        }
    }

    public void delete(String relativePath) {
        if (relativePath == null || relativePath.isBlank()) {
            return;
        }
        Path target = rootDir.resolve(relativePath).normalize();
        // Only ever delete inside the upload folder, whatever the stored path says.
        if (!target.startsWith(rootDir) || target.equals(rootDir)) {
            return;
        }
        try {
            Files.deleteIfExists(target);
        } catch (IOException ignored) {
            // best-effort cleanup; a leftover file is not worth failing the request over
        }
    }

    static String detectExtension(MultipartFile file) throws IOException {
        byte[] head;
        try (InputStream in = file.getInputStream()) {
            head = in.readNBytes(SIGNATURE_LENGTH);
        }
        return extensionFor(head);
    }

    static String extensionFor(byte[] head) {
        if (startsWith(head, 0xFF, 0xD8, 0xFF)) {
            return ".jpg";
        }
        if (startsWith(head, 0x89, 'P', 'N', 'G', 0x0D, 0x0A, 0x1A, 0x0A)) {
            return ".png";
        }
        if (startsWith(head, 'G', 'I', 'F', '8') && head.length >= 6
                && (head[4] == '7' || head[4] == '9') && head[5] == 'a') {
            return ".gif";
        }
        if (startsWith(head, 'R', 'I', 'F', 'F') && head.length >= 12
                && Arrays.equals(Arrays.copyOfRange(head, 8, 12), new byte[]{'W', 'E', 'B', 'P'})) {
            return ".webp";
        }
        return null;
    }

    private static boolean startsWith(byte[] data, int... signature) {
        if (data.length < signature.length) {
            return false;
        }
        for (int i = 0; i < signature.length; i++) {
            if ((data[i] & 0xFF) != signature[i]) {
                return false;
            }
        }
        return true;
    }
}

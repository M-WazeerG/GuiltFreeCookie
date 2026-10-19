package za.ac.cput.guiltfreecookie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import za.ac.cput.guiltfreecookie.domain.Cookie;
import za.ac.cput.guiltfreecookie.service.CookieService;
import za.ac.cput.guiltfreecookie.service.ImageStorageService;
import za.ac.cput.guiltfreecookie.util.Helper;

import java.util.List;

@RestController
@RequestMapping("/cookie")
public class CookieController {

    static final int MAX_TEXT_LENGTH = 255;
    static final int MAX_LONG_DESCRIPTION_LENGTH = 1000;
    static final double MAX_PRICE = 100_000;

    private final CookieService cookieService;
    private final ImageStorageService imageStorageService;

    @Autowired
    public CookieController(CookieService cookieService, ImageStorageService imageStorageService) {
        this.cookieService = cookieService;
        this.imageStorageService = imageStorageService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Cookie>> getAll() {
        return ResponseEntity.ok(cookieService.getAllActive());
    }

    @GetMapping("/getAllForAdmin")
    public ResponseEntity<List<Cookie>> getAllForAdmin() {
        return ResponseEntity.ok(cookieService.getAll());
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(
            @RequestPart("cookie") Cookie cookie,
            @RequestPart("image") MultipartFile image) {

        Cookie cleaned = clean(cookie);
        if (!isValidCookie(cleaned)) {
            return ResponseEntity.badRequest().build();
        }

        String imagePath;
        try {
            imagePath = imageStorageService.store(image);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        // The database assigns the id: a client-supplied one could overwrite another cookie.
        Cookie toCreate = new Cookie.Builder().copy(cleaned).setCookieId(null).setImage(imagePath).build();
        Cookie created = cookieService.create(toCreate);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Public: archived cookies are off the menu, so they don't exist here.
    @GetMapping("/read/{cookieId}")
    public ResponseEntity<Cookie> read(@PathVariable String cookieId) {
        Cookie cookie = cookieService.read(cookieId);
        if (cookie == null || cookie.isArchived()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(cookie);
    }

    @PutMapping(value = "/update/{cookieId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(
            @PathVariable String cookieId,
            @RequestPart("cookie") Cookie cookie,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        Cookie existing = cookieService.read(cookieId);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        Cookie cleaned = clean(cookie);
        if (!isValidCookie(cleaned)) {
            return ResponseEntity.badRequest().build();
        }

        String imagePath = existing.getImage();
        if (image != null && !image.isEmpty()) {
            try {
                imagePath = imageStorageService.store(image);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            imageStorageService.delete(existing.getImage());
        }

        Cookie toUpdate = new Cookie.Builder().copy(cleaned).setCookieId(cookieId).setImage(imagePath).build();
        Cookie updated = cookieService.update(toUpdate);

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{cookieId}")
    public ResponseEntity<Void> delete(@PathVariable String cookieId) {
        Cookie existing = cookieService.read(cookieId);
        cookieService.delete(cookieId);
        if (existing != null) {
            imageStorageService.delete(existing.getImage());
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/archive/{cookieId}")
    public ResponseEntity<Cookie> archive(@PathVariable String cookieId) {
        Cookie updated = cookieService.setArchived(cookieId, true);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/unarchive/{cookieId}")
    public ResponseEntity<Cookie> unarchive(@PathVariable String cookieId) {
        Cookie updated = cookieService.setArchived(cookieId, false);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    private static Cookie clean(Cookie cookie) {
        return new Cookie.Builder()
                .copy(cookie)
                .setDescription(Helper.clean(cookie.getDescription()))
                .setLongDescription(Helper.clean(cookie.getLongDescription()))
                .setIngredients(Helper.clean(cookie.getIngredients()))
                .setAllergies(Helper.clean(cookie.getAllergies()))
                .build();
    }

    // Lengths match the database columns, so bad input is a 400, not a 500.
    private static boolean isValidCookie(Cookie cookie) {
        return cookie.getCategory() != null
                && Helper.isPresent(cookie.getDescription(), MAX_TEXT_LENGTH)
                && Helper.isPresent(cookie.getIngredients(), MAX_TEXT_LENGTH)
                && Helper.isPresent(cookie.getAllergies(), MAX_TEXT_LENGTH)
                && Helper.isWithin(cookie.getLongDescription(), MAX_LONG_DESCRIPTION_LENGTH)
                && cookie.getPrice() != null
                && cookie.getPrice() > 0
                && cookie.getPrice() <= MAX_PRICE;
    }

}
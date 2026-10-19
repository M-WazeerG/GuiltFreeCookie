package za.ac.cput.guiltfreecookie.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.guiltfreecookie.domain.Cart;
import za.ac.cput.guiltfreecookie.domain.CartStatus;
import za.ac.cput.guiltfreecookie.factory.CartFactory;
import za.ac.cput.guiltfreecookie.service.CartService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    private Cart existingCart;

    @BeforeEach
    void setUp() {
        existingCart = CartFactory.createCart(
                "CRT001",
                "customer@guiltfreecookie.com",
                189.99,
                CartStatus.ACTIVE
        );
    }

    @Test
    void testCreateSuccess() {
        when(cartService.create(any(Cart.class))).thenReturn(existingCart);

        ResponseEntity<Cart> response = cartController.create(existingCart);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("CRT001", response.getBody().getCartId());
        verify(cartService).create(existingCart);
    }

    @Test
    void testCreateWithMissingCustomerEmailReturnsBadRequestAndNeverPersists() {
        Cart invalid = new Cart.Builder()
                .copy(existingCart)
                .setCustomerEmail("")
                .build();

        ResponseEntity<Cart> response = cartController.create(invalid);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(cartService, never()).create(any(Cart.class));
    }

    @Test
    void testCreateWithNegativePriceReturnsBadRequestAndNeverPersists() {
        Cart invalid = new Cart.Builder()
                .copy(existingCart)
                .setTotalPrice(-50.0)
                .build();

        ResponseEntity<Cart> response = cartController.create(invalid);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(cartService, never()).create(any(Cart.class));
    }

    @Test
    void testCreateWithNullStatusReturnsBadRequestAndNeverPersists() {
        Cart invalid = new Cart.Builder()
                .copy(existingCart)
                .setStatus(null)
                .build();

        ResponseEntity<Cart> response = cartController.create(invalid);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(cartService, never()).create(any(Cart.class));
    }

    @Test
    void testReadFound() {
        when(cartService.read("CRT001")).thenReturn(existingCart);

        ResponseEntity<Cart> response = cartController.read("CRT001");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("CRT001", response.getBody().getCartId());
        verify(cartService).read("CRT001");
    }

    @Test
    void testReadNotFound() {
        when(cartService.read("missing")).thenReturn(null);

        ResponseEntity<Cart> response = cartController.read("missing");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(cartService).read("missing");
    }

    @Test
    void testUpdateSuccess() {
        Cart updatedCart = new Cart.Builder()
                .copy(existingCart)
                .setStatus(CartStatus.CHECKED_OUT)
                .build();

        when(cartService.update(any(Cart.class))).thenReturn(updatedCart);

        ResponseEntity<Cart> response = cartController.update(updatedCart);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(CartStatus.CHECKED_OUT, response.getBody().getStatus());
        verify(cartService).update(updatedCart);
    }

    @Test
    void testUpdateWhenCartMissingReturnsNotFound() {
        when(cartService.update(any(Cart.class))).thenReturn(null);

        ResponseEntity<Cart> response = cartController.update(existingCart);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(cartService).update(existingCart);
    }

    @Test
    void testUpdateWithMissingCartIdReturnsBadRequestAndNeverPersists() {
        Cart invalid = new Cart.Builder()
                .copy(existingCart)
                .setCartId("")
                .build();

        ResponseEntity<Cart> response = cartController.update(invalid);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(cartService, never()).update(any(Cart.class));
    }

    @Test
    void testDeleteSuccess() {
        when(cartService.delete("CRT001")).thenReturn(true);

        ResponseEntity<Void> response = cartController.delete("CRT001");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(cartService).delete("CRT001");
    }

    @Test
    void testDeleteNotFound() {
        when(cartService.delete("missing")).thenReturn(false);

        ResponseEntity<Void> response = cartController.delete("missing");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(cartService).delete("missing");
    }

    @Test
    void testGetAll() {
        when(cartService.getAll()).thenReturn(List.of(existingCart));

        ResponseEntity<List<Cart>> response = cartController.getAll();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(cartService).getAll();
    }

    @Test
    void testGetByCustomer() {
        when(cartService.getCartsByCustomerEmail("customer@guiltfreecookie.com"))
                .thenReturn(List.of(existingCart));

        ResponseEntity<List<Cart>> response = cartController.getByCustomer("customer@guiltfreecookie.com");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(cartService).getCartsByCustomerEmail("customer@guiltfreecookie.com");
    }

    @Test
    void testGetByStatus() {
        when(cartService.getCartsByStatus(CartStatus.ACTIVE))
                .thenReturn(List.of(existingCart));

        ResponseEntity<List<Cart>> response = cartController.getByStatus(CartStatus.ACTIVE);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(cartService).getCartsByStatus(CartStatus.ACTIVE);
    }
}
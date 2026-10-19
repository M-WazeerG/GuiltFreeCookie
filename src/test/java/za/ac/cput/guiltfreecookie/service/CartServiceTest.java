package za.ac.cput.guiltfreecookie.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.ac.cput.guiltfreecookie.domain.Cart;
import za.ac.cput.guiltfreecookie.domain.CartStatus;
import za.ac.cput.guiltfreecookie.factory.CartFactory;
import za.ac.cput.guiltfreecookie.repository.CartRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartService cartService;

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
    void testCreate() {
        when(cartRepository.save(existingCart)).thenReturn(existingCart);

        Cart result = cartService.create(existingCart);

        assertNotNull(result);
        assertEquals("CRT001", result.getCartId());
        assertEquals("customer@guiltfreecookie.com", result.getCustomerEmail());
        verify(cartRepository).save(existingCart);
    }

    @Test
    void testReadFound() {
        when(cartRepository.findById("CRT001")).thenReturn(Optional.of(existingCart));

        Cart result = cartService.read("CRT001");

        assertNotNull(result);
        assertEquals("CRT001", result.getCartId());
        assertEquals(189.99, result.getTotalPrice());
        verify(cartRepository).findById("CRT001");
    }

    @Test
    void testReadNotFound() {
        when(cartRepository.findById("missing")).thenReturn(Optional.empty());

        Cart result = cartService.read("missing");

        assertNull(result);
        verify(cartRepository).findById("missing");
    }

    @Test
    void testUpdateWhenFoundMergesFields() {
        Cart incoming = new Cart.Builder()
                .copy(existingCart)
                .setTotalPrice(249.99)
                .setStatus(CartStatus.CHECKED_OUT)
                .build();

        when(cartRepository.findById("CRT001")).thenReturn(Optional.of(existingCart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart result = cartService.update(incoming);

        assertNotNull(result);
        assertEquals("CRT001", result.getCartId());
        assertEquals(249.99, result.getTotalPrice());
        assertEquals(CartStatus.CHECKED_OUT, result.getStatus());
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void testUpdateWhenNotFoundReturnsNull() {
        when(cartRepository.findById("missing")).thenReturn(Optional.empty());

        Cart incoming = new Cart.Builder()
                .setCartId("missing")
                .setCustomerEmail("unknown@guiltfreecookie.com")
                .setTotalPrice(100.0)
                .setStatus(CartStatus.ACTIVE)
                .build();

        Cart result = cartService.update(incoming);

        assertNull(result);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void testDeleteWhenExists() {
        when(cartRepository.existsById("CRT001")).thenReturn(true);

        boolean result = cartService.delete("CRT001");

        assertTrue(result);
        verify(cartRepository).deleteById("CRT001");
    }

    @Test
    void testDeleteWhenNotExists() {
        when(cartRepository.existsById("missing")).thenReturn(false);

        boolean result = cartService.delete("missing");

        assertFalse(result);
        verify(cartRepository, never()).deleteById(anyString());
    }

    @Test
    void testGetAllReturnsEveryCart() {
        Cart secondCart = CartFactory.createCart(
                "CRT002",
                "another@guiltfreecookie.com",
                95.00,
                CartStatus.CHECKED_OUT
        );

        when(cartRepository.findAll()).thenReturn(List.of(existingCart, secondCart));

        List<Cart> result = cartService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(cartRepository).findAll();
    }

    @Test
    void testGetCartsByCustomerEmail() {
        when(cartRepository.findByCustomerEmail("customer@guiltfreecookie.com"))
                .thenReturn(List.of(existingCart));

        List<Cart> result = cartService.getCartsByCustomerEmail("customer@guiltfreecookie.com");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("customer@guiltfreecookie.com", result.get(0).getCustomerEmail());
        verify(cartRepository).findByCustomerEmail("customer@guiltfreecookie.com");
    }

    @Test
    void testGetCartsByStatus() {
        when(cartRepository.findByStatus(CartStatus.ACTIVE))
                .thenReturn(List.of(existingCart));

        List<Cart> result = cartService.getCartsByStatus(CartStatus.ACTIVE);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(CartStatus.ACTIVE, result.get(0).getStatus());
        verify(cartRepository).findByStatus(CartStatus.ACTIVE);
    }
}
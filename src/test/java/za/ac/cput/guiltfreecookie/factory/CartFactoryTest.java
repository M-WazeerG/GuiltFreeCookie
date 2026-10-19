package za.ac.cput.guiltfreecookie.factory;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import za.ac.cput.guiltfreecookie.domain.Cart;
import za.ac.cput.guiltfreecookie.domain.CartStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CartFactoryTest {

    @Test
    @Order(1)
    void testCreateCartSuccess() {
        Cart cart = CartFactory.createCart(
                "CRT001",
                "customer@guiltfreecookie.com",
                189.99,
                CartStatus.ACTIVE
        );

        assertNotNull(cart);
        assertEquals("CRT001", cart.getCartId());
        assertEquals("customer@guiltfreecookie.com", cart.getCustomerEmail());
        assertEquals(189.99, cart.getTotalPrice());
        assertEquals(CartStatus.ACTIVE, cart.getStatus());

        System.out.println(cart);
    }

    @Test
    @Order(2)
    void testCreateCartWithGeneratedIdSuccess() {
        Cart cart = CartFactory.createCart(
                "customer@guiltfreecookie.com",
                250.00,
                CartStatus.ACTIVE
        );

        assertNotNull(cart);
        assertNotNull(cart.getCartId());
        assertFalse(cart.getCartId().isEmpty());
        assertEquals("customer@guiltfreecookie.com", cart.getCustomerEmail());
        assertEquals(250.00, cart.getTotalPrice());
        assertEquals(CartStatus.ACTIVE, cart.getStatus());

        System.out.println("Generated ID Cart: " + cart);
    }

    @Test
    @Order(3)
    void testCreateCartWithNullCartId() {
        Cart cart = CartFactory.createCart(
                null,
                "customer@guiltfreecookie.com",
                189.99,
                CartStatus.ACTIVE
        );

        assertNull(cart);
        System.out.println("Null cart ID test passed: " + cart);
    }

    @Test
    @Order(4)
    void testCreateCartWithEmptyCartId() {
        Cart cart = CartFactory.createCart(
                "",
                "customer@guiltfreecookie.com",
                189.99,
                CartStatus.ACTIVE
        );

        assertNull(cart);
        System.out.println("Empty cart ID test passed: " + cart);
    }

    @Test
    @Order(5)
    void testCreateCartWithNullCustomerEmail() {
        Cart cart = CartFactory.createCart(
                "CRT002",
                null,
                189.99,
                CartStatus.ACTIVE
        );

        assertNull(cart);
        System.out.println("Null email test passed: " + cart);
    }

    @Test
    @Order(6)
    void testCreateCartWithEmptyCustomerEmail() {
        Cart cart = CartFactory.createCart(
                "CRT003",
                "",
                189.99,
                CartStatus.ACTIVE
        );

        assertNull(cart);
        System.out.println("Empty email test passed: " + cart);
    }

    @Test
    @Order(7)
    void testCreateCartWithNegativeTotalPrice() {
        Cart cart = CartFactory.createCart(
                "CRT004",
                "customer@guiltfreecookie.com",
                -25.00,
                CartStatus.ACTIVE
        );

        assertNull(cart);
        System.out.println("Negative price test passed: " + cart);
    }

    @Test
    @Order(8)
    void testCreateCartWithNullStatus() {
        Cart cart = CartFactory.createCart(
                "CRT005",
                "customer@guiltfreecookie.com",
                189.99,
                null
        );

        assertNull(cart);
        System.out.println("Null status test passed: " + cart);
    }
}
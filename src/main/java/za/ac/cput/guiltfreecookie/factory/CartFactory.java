package za.ac.cput.guiltfreecookie.factory;

import za.ac.cput.guiltfreecookie.domain.Cart;
import za.ac.cput.guiltfreecookie.domain.CartStatus;
import za.ac.cput.guiltfreecookie.util.Helper;

import java.util.UUID;

public class CartFactory {

    public static Cart createCart(String cartId, String customerEmail, double totalPrice, CartStatus status) {
        if (Helper.isNullOrEmpty(cartId)
                || Helper.isNullOrEmpty(customerEmail)
                || totalPrice < 0
                || status == null) {
            return null;
        }

        return new Cart.Builder()
                .setCartId(cartId)
                .setCustomerEmail(customerEmail)
                .setTotalPrice(totalPrice)
                .setStatus(status)
                .build();
    }

    public static Cart createCart(String customerEmail, double totalPrice, CartStatus status) {
        if (Helper.isNullOrEmpty(customerEmail)
                || totalPrice < 0
                || status == null) {
            return null;
        }

        String cartId = UUID.randomUUID().toString();

        return new Cart.Builder()
                .setCartId(cartId)
                .setCustomerEmail(customerEmail)
                .setTotalPrice(totalPrice)
                .setStatus(status)
                .build();
    }
}
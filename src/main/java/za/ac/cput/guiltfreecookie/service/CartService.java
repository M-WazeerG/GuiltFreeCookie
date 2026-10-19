package za.ac.cput.guiltfreecookie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.guiltfreecookie.domain.Cart;
import za.ac.cput.guiltfreecookie.domain.CartStatus;
import za.ac.cput.guiltfreecookie.repository.CartRepository;

import java.util.List;

@Service
public class CartService implements IService<Cart, String> {

    private final CartRepository cartRepository;

    @Autowired
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart create(Cart cart) {
        return this.cartRepository.save(cart);
    }

    @Override
    public Cart read(String id) {
        return this.cartRepository.findById(id).orElse(null);
    }

    @Override
    public Cart update(Cart cart) {
        Cart existing = this.cartRepository.findById(cart.getCartId()).orElse(null);
        if (existing == null) {
            return null;
        }

        Cart updated = new Cart.Builder()
                .copy(existing)
                .setCustomerEmail(cart.getCustomerEmail())
                .setTotalPrice(cart.getTotalPrice())
                .setStatus(cart.getStatus())
                .build();

        return this.cartRepository.save(updated);
    }

    @Override
    public boolean delete(String id) {
        if (this.cartRepository.existsById(id)) {
            this.cartRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Cart> getAll() {
        return this.cartRepository.findAll();
    }

    public List<Cart> getCartsByCustomerEmail(String customerEmail) {
        return this.cartRepository.findByCustomerEmail(customerEmail);
    }

    public List<Cart> getCartsByStatus(CartStatus status) {
        return this.cartRepository.findByStatus(status);
    }
}
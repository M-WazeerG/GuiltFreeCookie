package za.ac.cput.guiltfreecookie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.guiltfreecookie.domain.Cart;
import za.ac.cput.guiltfreecookie.domain.CartStatus;
import za.ac.cput.guiltfreecookie.service.CartService;
import za.ac.cput.guiltfreecookie.util.Helper;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/create")
    public ResponseEntity<Cart> create(@RequestBody Cart cart) {
        if (cart == null
                || Helper.isNullOrEmpty(cart.getCustomerEmail())
                || cart.getTotalPrice() < 0
                || cart.getStatus() == null) {
            return ResponseEntity.badRequest().build();
        }

        Cart created = cartService.create(cart);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Cart> read(@PathVariable String id) {
        Cart cart = cartService.read(id);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/update")
    public ResponseEntity<Cart> update(@RequestBody Cart cart) {
        if (cart == null
                || Helper.isNullOrEmpty(cart.getCartId())
                || Helper.isNullOrEmpty(cart.getCustomerEmail())
                || cart.getTotalPrice() < 0
                || cart.getStatus() == null) {
            return ResponseEntity.badRequest().build();
        }

        Cart updated = cartService.update(cart);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = cartService.delete(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Cart>> getAll() {
        return ResponseEntity.ok(cartService.getAll());
    }

    @GetMapping("/customer/{customerEmail}")
    public ResponseEntity<List<Cart>> getByCustomer(@PathVariable String customerEmail) {
        return ResponseEntity.ok(cartService.getCartsByCustomerEmail(customerEmail));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Cart>> getByStatus(@PathVariable CartStatus status) {
        return ResponseEntity.ok(cartService.getCartsByStatus(status));
    }
}
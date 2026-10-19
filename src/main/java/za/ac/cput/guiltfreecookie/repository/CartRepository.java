package za.ac.cput.guiltfreecookie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.guiltfreecookie.domain.Cart;
import za.ac.cput.guiltfreecookie.domain.CartStatus;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {

    List<Cart> findByCustomerEmail(String customerEmail);

    List<Cart> findByStatus(CartStatus status);
}
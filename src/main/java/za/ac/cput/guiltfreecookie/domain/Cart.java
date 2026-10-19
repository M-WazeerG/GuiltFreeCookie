package za.ac.cput.guiltfreecookie.domain;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @Column(name = "cart_id")
    private String cartId;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "total_price")
    private double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CartStatus status;

    protected Cart() {
    }

    private Cart(Builder builder) {
        this.cartId = builder.cartId;
        this.customerEmail = builder.customerEmail;
        this.totalPrice = builder.totalPrice;
        this.status = builder.status;
    }

    public String getCartId() {
        return cartId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public CartStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cart cart)) return false;
        return Double.compare(cart.totalPrice, totalPrice) == 0 &&
                Objects.equals(cartId, cart.cartId) &&
                Objects.equals(customerEmail, cart.customerEmail) &&
                status == cart.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartId, customerEmail, totalPrice, status);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartId='" + cartId + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", totalPrice=" + totalPrice +
                ", status=" + status +
                '}';
    }

    public static class Builder {
        private String cartId;
        private String customerEmail;
        private double totalPrice;
        private CartStatus status;

        public Builder setCartId(String cartId) {
            this.cartId = cartId;
            return this;
        }

        public Builder setCustomerEmail(String customerEmail) {
            this.customerEmail = customerEmail;
            return this;
        }

        public Builder setTotalPrice(double totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public Builder setStatus(CartStatus status) {
            this.status = status;
            return this;
        }

        public Builder copy(Cart cart) {
            this.cartId = cart.cartId;
            this.customerEmail = cart.customerEmail;
            this.totalPrice = cart.totalPrice;
            this.status = cart.status;
            return this;
        }

        public Cart build() {
            return new Cart(this);
        }
    }
}
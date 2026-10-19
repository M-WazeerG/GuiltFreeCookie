package za.ac.cput.guiltfreecookie.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * Someone who asked for marketing email. Keeping when they opted in is part
 * of the POPIA record of consent.
 */
@Entity
@Table(name = "newsletter_subscribers")
public class NewsletterSubscriber {

    @Id
    @Column(length = 254)
    private String email;

    @Column(nullable = false)
    private Instant subscribedAt;

    protected NewsletterSubscriber() {
    }

    public NewsletterSubscriber(String email, Instant subscribedAt) {
        this.email = email;
        this.subscribedAt = subscribedAt;
    }

    public String getEmail() {
        return email;
    }

    public Instant getSubscribedAt() {
        return subscribedAt;
    }
}

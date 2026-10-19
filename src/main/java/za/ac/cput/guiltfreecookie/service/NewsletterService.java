package za.ac.cput.guiltfreecookie.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import za.ac.cput.guiltfreecookie.domain.NewsletterSubscriber;
import za.ac.cput.guiltfreecookie.repository.NewsletterSubscriberRepository;

import java.time.Instant;
import java.util.List;

@Service
public class NewsletterService {

    private final NewsletterSubscriberRepository repository;

    public NewsletterService(NewsletterSubscriberRepository repository) {
        this.repository = repository;
    }

    /** Idempotent: subscribing twice keeps the original opt-in date. */
    public void subscribe(String email) {
        if (!repository.existsById(email)) {
            repository.save(new NewsletterSubscriber(email, Instant.now()));
        }
    }

    public List<NewsletterSubscriber> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "subscribedAt"));
    }
}

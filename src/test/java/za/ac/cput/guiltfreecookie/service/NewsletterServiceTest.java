package za.ac.cput.guiltfreecookie.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.ac.cput.guiltfreecookie.domain.NewsletterSubscriber;
import za.ac.cput.guiltfreecookie.repository.NewsletterSubscriberRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NewsletterServiceTest {

    @Mock
    private NewsletterSubscriberRepository repository;

    @InjectMocks
    private NewsletterService newsletterService;

    @Test
    void savesANewSubscriberWithTheirOptInTime() {
        when(repository.existsById("thandi@example.co.za")).thenReturn(false);

        newsletterService.subscribe("thandi@example.co.za");

        verify(repository).save(argThat(s -> s.getEmail().equals("thandi@example.co.za") && s.getSubscribedAt() != null));
    }

    @Test
    void keepsTheOriginalOptInWhenSubscribingAgain() {
        when(repository.existsById("thandi@example.co.za")).thenReturn(true);

        newsletterService.subscribe("thandi@example.co.za");

        verify(repository, never()).save(any(NewsletterSubscriber.class));
    }
}

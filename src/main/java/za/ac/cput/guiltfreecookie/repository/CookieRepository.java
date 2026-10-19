package za.ac.cput.guiltfreecookie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.guiltfreecookie.domain.Cookie;

import java.util.List;

@Repository
public interface CookieRepository extends JpaRepository<Cookie, String> {

    List<Cookie> findByArchivedFalse();

}
package za.ac.cput.guiltfreecookie.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.time.Duration;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String uploadRootDir;

    public WebConfig(@Value("${app.upload.root-dir}") String uploadRootDir) {
        this.uploadRootDir = uploadRootDir;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = "file:" + Path.of(uploadRootDir).toAbsolutePath().normalize() + "/";
        // Every upload gets a fresh random name and is never rewritten, so
        // browsers and CDNs can keep it for a year.
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(location)
                .setCacheControl(CacheControl.maxAge(Duration.ofDays(365)).cachePublic().immutable());
    }
}

package nl.dylanvdbrink.flixtraktor.watchedtitleenricher;

import lombok.extern.apachecommons.CommonsLog;
import nl.dylanvdbrink.flixtraktor.watchedtitleenricher.trakt.TraktAuthenticationService;
import nl.dylanvdbrink.flixtraktor.watchedtitleenricher.trakt.pojo.TraktAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.IOException;

@SpringBootApplication
@CommonsLog
public class WatchedtitleEnricherApplication {

    private final TraktAuthenticationService traktAuthService;

    private final ApplicationPropertyService propertyService;

    @Autowired
    public WatchedtitleEnricherApplication(TraktAuthenticationService traktAuthService, ApplicationPropertyService propertyService) {
        this.traktAuthService = traktAuthService;
        this.propertyService = propertyService;
    }

    public static void main(String[] args) {
        SpringApplication.run(WatchedtitleEnricherApplication.class, args);
    }

    @PostConstruct
    private void init() {
        try {
            propertyService.setTraktAccessToken(traktAuthService.doInitialTraktAuthentication());
        } catch (TraktAuthException | IOException e) {
            log.error("Could not authenticate with Trakt", e);
        }
    }

}

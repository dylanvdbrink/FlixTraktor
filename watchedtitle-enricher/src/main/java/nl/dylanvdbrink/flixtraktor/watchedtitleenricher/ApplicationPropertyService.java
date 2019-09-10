package nl.dylanvdbrink.flixtraktor.watchedtitleenricher;

import lombok.Data;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Data
public class ApplicationPropertyService {

    private String traktAccessToken;

}

package nl.dylanvdbrink.flixtraktor.watchedtitleenricher;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.config.SimpleJmsListenerContainerFactory;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.jms.ConnectionFactory;
import java.util.Objects;

@Component
@CommonsLog
public class NetflixWatchedTitleConsumer {

    private final ApplicationContext context;

    @Autowired
    public NetflixWatchedTitleConsumer(ApplicationContext context) {
        this.context = context;
    }

    @Bean
    public JmsListenerContainerFactory<?> queueContainerFactory(ActiveMQConnectionFactory cf) {
        cf.setTrustAllPackages(true);
        SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
        factory.setConnectionFactory(cf);
        factory.setAutoStartup(false);
        return factory;
    }

    public void startListening() {
        log.info("Started listening for netlix titles");
        JmsListenerEndpointRegistry reg = context.getBean(JmsListenerEndpointRegistry.class);
        MessageListenerContainer listenerContainer = reg.getListenerContainer("TitleMessageConsumer");
        Objects.requireNonNull(listenerContainer, "listenerContainer was null");
        listenerContainer.start();
    }

    @JmsListener(id = "TitleMessageConsumer", destination = "${activemq.viewing-activity-topic}", containerFactory = "queueContainerFactory")
    public void receiveMessage(String message) {
        JsonObject jsonObject = new JsonParser().parse(message).getAsJsonObject();

        // Title is movie
        if (jsonObject.get("series").getAsInt() == 0) {
            // Query for movie

        }
        // Title is episode
        else {
            // Query for tv show
            log.info("Title " + jsonObject.get("seriesTitle").getAsString() + " " + jsonObject.get("videoTitle").getAsString()
                    + ", watched on " + jsonObject.get("dateStr").getAsString() + " is an episode.");
        }

        // Call Trakt to search for title and get the result

        // Send enriched title to queue
    }

}

package fr.univ.angers.quizz.api.websocket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
// Configure Spring pour activer le webSocket et STOMP
public class WebSocketBrokerConfig implements WebSocketMessageBrokerConfigurer {

    private Logger logger = LogManager.getLogger(WebSocketBrokerConfig.class);

    // Configure le message broker
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        logger.info("WebSocketBrokerConfig : configureMessageBroker()");

        // Définit le préfixes de destination pour l'envoi et la réception de messages
        registry.enableSimpleBroker("/quiz");

        // Définit le préfixe qui est utilisée pour filtrer les destinations traitées par des méthodes annotées avec @MessageMapping
        registry.setApplicationDestinationPrefixes("/app");
    }

    // Enregistre l'endpoint /ws : point de connexion pour ouvrir un websocket
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        logger.info("WebSocketBrokerConfig : registerStompEndpoints()");
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
    }


}

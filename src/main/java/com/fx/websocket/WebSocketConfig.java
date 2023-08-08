package com.fx.websocket;

import com.fx.ApplicationConfiguration;
import com.fx.websocket.endpoints.SubscriptionEndPoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer    {

    @Autowired
    ApplicationConfiguration config;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(SubscriptionEndPoints.SIMPLE_BROKER_DESTINATION_PREFIXES);
        registry.setApplicationDestinationPrefixes(config.aplication_destination_prefixes);
        System.out.println("configured Message Broker");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(config.stompEndpoint)
                .setAllowedOrigins(config.allowed_origins.split(","))
                .withSockJS();
        System.out.println("WebSocketConfig configured StompEndpoints");
    }

}
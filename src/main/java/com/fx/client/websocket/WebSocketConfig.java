package com.fx.client.websocket;

import com.fx.common.ApplicationConfiguration;
import com.fx.client.service.StompHandshakeHandler;
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

    @Autowired
    StompHandshakeHandler stompHandshakeHandler;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(config.simple_broker_destination_prefixes.split(","));
        registry.setApplicationDestinationPrefixes(config.aplication_destination_prefixes);
        System.out.println("configured Message Broker with enableSimpleBroker");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(config.stompEndpoint)
                .setAllowedOrigins(config.allowed_origins.split(","))
                .setHandshakeHandler(stompHandshakeHandler)
                .withSockJS();
        System.out.println("WebSocketConfig configured StompEndpoints");
    }

}
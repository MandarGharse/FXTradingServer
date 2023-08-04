package com.fx.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig extends WebSocketMessageBrokerConfigurationSupport implements WebSocketConfigurer    {

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        System.out.println("registering WebSocketHandlers");
        registry.addHandler(new SocketTextHandler(), "/user");
        System.out.println("registered WebSocketHandlers");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/user")
                .withSockJS()
                .setStreamBytesLimit(512 * 1024)
                .setHttpMessageCacheSize(10 * 1024);
        System.out.println("WebSocketConfig configured StompEndpoints");
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setMessageSizeLimit(8388608);
        registry.setSendBufferSizeLimit(16777216);
        System.out.println("WebSocketConfig configureWebSocketTransport complete!");
    }

}
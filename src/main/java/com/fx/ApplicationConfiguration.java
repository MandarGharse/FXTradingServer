package com.fx;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:environment-independent.properties")
public class ApplicationConfiguration {

    @Value("${websocket.allowed-origins}")
    public String allowed_origins;

    @Value("${websocket.application-destination-prefixes}")
    public String aplication_destination_prefixes;

    @Value("${websocket.stomp-endpoint}")
    public String stompEndpoint;

    @Value("${websocket.simple-broker-destination-prefixes}")
    public String simple_broker_destination_prefixes;

}

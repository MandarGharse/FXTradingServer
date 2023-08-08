package com.fx.controller;

import com.fx.ApplicationConfiguration;
import com.fx.domain.GreetingDto;
import com.fx.websocket.StompPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Map;

import static com.fx.websocket.endpoints.SubscriptionEndPoints.GREET_ENDPOINT;
import static com.fx.websocket.endpoints.SubscriptionEndPoints.SIMPLE_BROKER_DESTINATION_PREFIXES;

@Controller
public class GreetingController {

    @Autowired
    ApplicationConfiguration config;

    @MessageMapping(GREET_ENDPOINT)
    @SendTo(SIMPLE_BROKER_DESTINATION_PREFIXES + "/greetings")
    public String greeting(String greetingDto, StompPrincipal stompPrincipal, @Headers Map headers)   {
        try {
            System.out.println("client message recvd : " + greetingDto);
            System.out.println("stompPrincipal >>> " + stompPrincipal);
            System.out.println("headers >>> " + headers);

            return "Hello, " + greetingDto;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

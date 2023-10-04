package com.fx.controller;

import com.fx.client.handlers.PricingSubscriptionRequestHandler;
import com.fx.client.service.StompEnhancedMessageSender;
import com.fx.client.session.UserSession;
import com.fx.client.session.UserSessionManager;
import com.fx.client.websocket.StompPrincipal;
import com.fx.common.ApplicationConfiguration;
import com.fx.common.utils.ObjectMapperUtil;
import com.fx.domain.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Map;

import static com.fx.client.websocket.endpoints.SubscriptionEndPoints.*;

@Controller
public class PricingStompController {

    @Autowired
    ApplicationConfiguration config;

    @Autowired
    StompEnhancedMessageSender stompEnhancedMessageSender;

    @Autowired
    PricingSubscriptionRequestHandler pricingSubscriptionRequestHandler;

    @MessageMapping(PRICING_SUBSCRIPTION_ENDPOINT)
    public void handlePricingSubscriptionRequest(@Payload PricingSubscriptionRequestMessage pricingSubscriptionRequestMessage,
                                                 StompPrincipal stompPrincipal, @Headers Map headers)   {

        try {
            System.out.println("pricingSubscriptionRequestMessage recvd : " + pricingSubscriptionRequestMessage);
            System.out.println("stompPrincipal >>> " + stompPrincipal);
            System.out.println("headers >>> " + headers);

            UserSession userSession = UserSessionManager.getUserSession(stompPrincipal.getName());
            System.out.println("retrieved userSession : " + userSession);
            if (userSession == null)    {
                PricingSubscriptionResponseMessage pricingSubscriptionResponseMessage = new PricingSubscriptionResponseMessage();
                pricingSubscriptionResponseMessage.setSessionId(pricingSubscriptionRequestMessage.getSessionId());
                pricingSubscriptionResponseMessage.setId(pricingSubscriptionRequestMessage.getId());
                pricingSubscriptionResponseMessage.setCcyPair(pricingSubscriptionRequestMessage.getCcyPair());
                pricingSubscriptionResponseMessage.setValueDate(pricingSubscriptionRequestMessage.getValueDate());
                pricingSubscriptionResponseMessage.setStatus("NACK");
                pricingSubscriptionResponseMessage.setRejectText("Invalid sessionId " + pricingSubscriptionResponseMessage.getSessionId() + ". not found");
                stompEnhancedMessageSender.sendMessage(stompPrincipal.getName(),
                        ObjectMapperUtil.objectMapper.writeValueAsString(pricingSubscriptionResponseMessage),PRICING_SUBSCRIPTION_ENDPOINT);
                return;
            }
            userSession.setPricingSubscriptionRequestMessage(pricingSubscriptionRequestMessage);

            pricingSubscriptionRequestHandler.onPricingSubscriptionRequest(pricingSubscriptionRequestMessage, stompPrincipal);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        System.out.println("Pricing Exception : " + exception);
        return exception.getMessage();
    }

}

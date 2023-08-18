package com.fx.controller;

import com.fx.common.ApplicationConfiguration;
import com.fx.domain.json.*;
import com.fx.client.handlers.FXTradesRequestHandler;
import com.fx.client.service.StompEnhancedMessageSender;
import com.fx.client.session.UserSession;
import com.fx.client.session.UserSessionManager;
import com.fx.common.utils.ObjectMapperUtil;
import com.fx.client.websocket.StompPrincipal;
import com.fx.client.websocket.endpoints.SubscriptionEndPoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.util.Map;

import static com.fx.client.websocket.endpoints.SubscriptionEndPoints.BOOKING_ENDPOINT;
import static com.fx.client.websocket.endpoints.SubscriptionEndPoints.FXTRADES_SUBSCRIPTION_ENDPOINT;

@Controller
public class StompController {

    @Autowired
    ApplicationConfiguration config;

    @Autowired
    StompEnhancedMessageSender stompEnhancedMessageSender;

    @Autowired
    FXTradesRequestHandler fxTradesRequestHandler;

    @MessageMapping(BOOKING_ENDPOINT)
    public void handleBookingRequest(@Payload FxTrade trade, StompPrincipal stompPrincipal, @Headers Map headers)   {

        try {
            System.out.println("Booking Request recvd : " + trade);
            System.out.println("stompPrincipal >>> " + stompPrincipal);
            System.out.println("headers >>> " + headers);
            System.out.printf("trade " + ObjectMapperUtil.objectMapper.writeValueAsString(trade));

            stompEnhancedMessageSender.sendMessage(stompPrincipal.getName(),
                    ObjectMapperUtil.objectMapper.writeValueAsString(trade), BOOKING_ENDPOINT);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @MessageMapping(FXTRADES_SUBSCRIPTION_ENDPOINT)
    public void handleFXTradesSubscriptionRequest(@Payload FXTradesSubscriptionRequest fxTradesSubscriptionRequest, StompPrincipal stompPrincipal, @Headers Map headers)   {

        try {
            System.out.println("fxTradesSubscriptionRequestt recvd : " + fxTradesSubscriptionRequest);
            System.out.println("stompPrincipal >>> " + stompPrincipal);
            System.out.println("headers >>> " + headers);

            UserSession userSession = UserSessionManager.getUserSession(stompPrincipal.getName());
            System.out.println("retrieved userSession : " + userSession);
            if (userSession == null)    {
                FXTradesSubscriptionResponse fxTradesSubscriptionResponse = new FXTradesSubscriptionResponse();
                fxTradesSubscriptionResponse.setSessionId(fxTradesSubscriptionRequest.getSessionId());
                fxTradesSubscriptionResponse.setType(fxTradesSubscriptionRequest.getType());
                fxTradesSubscriptionResponse.setStatus("NACK");
                fxTradesSubscriptionResponse.setRejectText("Invalid sessionId " + fxTradesSubscriptionRequest.getSessionId() + ". not found");
                stompEnhancedMessageSender.sendMessage(stompPrincipal.getName(),
                        ObjectMapperUtil.objectMapper.writeValueAsString(fxTradesSubscriptionResponse), FXTRADES_SUBSCRIPTION_ENDPOINT);
                return;
            }
            userSession.setFXTradesRequest(fxTradesSubscriptionRequest);

            fxTradesRequestHandler.onFXTradesSubscriptionRequest(fxTradesSubscriptionRequest, stompPrincipal);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    @MessageMapping(FXTRADES_VIEWPORT_ENDPOINT)
//    public void handleFXTradesSubscriptionRequest(@Payload FXTradesSubscriptionRequest fxTradesSubscriptionRequest, StompPrincipal stompPrincipal, @Headers Map headers)   {
//
//        try {
//            System.out.println("fxTradesSubscriptionRequestt recvd : " + fxTradesSubscriptionRequest);
//            System.out.println("stompPrincipal >>> " + stompPrincipal);
//            System.out.println("headers >>> " + headers);
//
//            UserSession userSession = UserSessionManager.getUserSession(stompPrincipal.getName());
//            System.out.println("retrieved userSession : " + userSession);
//            if (userSession == null)    {
//                FXTradesResponse fxTradesResponse = new FXTradesResponse();
//                fxTradesResponse.setSessionId(fxTradesSubscriptionRequest.getSessionId());
//                fxTradesResponse.setStartIndex(fxTradesRequest.getStartIndex());
//                fxTradesResponse.setEndIndex(fxTradesRequest.getEndIndex());
//                fxTradesResponse.setStatus("NACK");
//                fxTradesResponse.setRejectText("Invalid sessionId " + fxTradesRequest.getSessionId() + ". not found");
//                stompEnhancedMessageSender.sendMessage(stompPrincipal.getName(),
//                        ObjectMapperUtil.objectMapper.writeValueAsString(fxTradesResponse), SubscriptionEndPoints.FXTRADES_SUBSCRIPTION_ENDPOINT);
//                return;
//            }
//            userSession.setFXTradesRequest(fxTradesRequest);
//
//            fxTradesRequestHandler.onData(fxTradesRequest, stompPrincipal);
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

}

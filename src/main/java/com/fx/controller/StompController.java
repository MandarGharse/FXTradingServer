package com.fx.controller;

import com.fx.ApplicationConfiguration;
import com.fx.domain.FXTradesRequest;
import com.fx.domain.FXTradesResponse;
import com.fx.domain.FxTrade;
import com.fx.handlers.FXTradesRequestHandler;
import com.fx.service.StompEnhancedMessageSender;
import com.fx.session.UserSession;
import com.fx.session.UserSessionManager;
import com.fx.utils.ObjectMapperUtil;
import com.fx.websocket.StompPrincipal;
import com.fx.websocket.endpoints.SubscriptionEndPoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.util.Map;

import static com.fx.websocket.endpoints.SubscriptionEndPoints.BOOKING_ENDPOINT;
import static com.fx.websocket.endpoints.SubscriptionEndPoints.FXTRADES_ENDPOINT;

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
                    ObjectMapperUtil.objectMapper.writeValueAsString(trade), SubscriptionEndPoints.BOOKING_ENDPOINT);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @MessageMapping(FXTRADES_ENDPOINT)
    public void handleFXTradesRequest(@Payload FXTradesRequest fxTradesRequest, StompPrincipal stompPrincipal, @Headers Map headers)   {

        try {
            System.out.println("FXTrades Request recvd : " + fxTradesRequest);
            System.out.println("stompPrincipal >>> " + stompPrincipal);
            System.out.println("headers >>> " + headers);

            UserSession userSession = UserSessionManager.getUserSession(stompPrincipal.getName());
            System.out.println("retrieved userSession : " + userSession);
            if (userSession == null)    {
                FXTradesResponse fxTradesResponse = new FXTradesResponse();
                fxTradesResponse.setSessionId(fxTradesRequest.getSessionId());
                fxTradesResponse.setStartIndex(fxTradesRequest.getStartIndex());
                fxTradesResponse.setEndIndex(fxTradesRequest.getEndIndex());
                fxTradesResponse.setStatus("NACK");
                fxTradesResponse.setRejectText("Invalid sessionId " + fxTradesRequest.getSessionId() + ". not found");
                stompEnhancedMessageSender.sendMessage(stompPrincipal.getName(),
                        ObjectMapperUtil.objectMapper.writeValueAsString(fxTradesResponse), SubscriptionEndPoints.FXTRADES_ENDPOINT);
                return;
            }
            userSession.setFXTradesRequest(fxTradesRequest);

            fxTradesRequestHandler.onData(fxTradesRequest, stompPrincipal);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

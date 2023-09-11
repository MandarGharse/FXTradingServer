package com.fx.controller;

import com.fx.common.ApplicationConfiguration;
import com.fx.domain.json.*;
import com.fx.client.handlers.PortfolioSubscriptionRequestHandler;
import com.fx.client.service.StompEnhancedMessageSender;
import com.fx.client.session.UserSession;
import com.fx.client.session.UserSessionManager;
import com.fx.common.utils.ObjectMapperUtil;
import com.fx.client.websocket.StompPrincipal;
import com.fx.proto.messaging.TradeMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Map;

import static com.fx.client.websocket.endpoints.SubscriptionEndPoints.*;

@Controller
public class StompController {

    @Autowired
    ApplicationConfiguration config;

    @Autowired
    StompEnhancedMessageSender stompEnhancedMessageSender;

    @Autowired
    PortfolioSubscriptionRequestHandler portfolioSubscriptionRequestHandler;

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

    @MessageMapping(BLOTTER_SUBSCRIPTION_ENDPOINT)
    public void handleBlotterSubscriptionRequest(@Payload BlotterSubscriptionRequestMessage blotterSubscriptionRequestMessage,
                                                 StompPrincipal stompPrincipal, @Headers Map headers)   {

        try {
            System.out.println("blotterSubscriptionRequestMessage recvd : " + blotterSubscriptionRequestMessage);
            System.out.println("stompPrincipal >>> " + stompPrincipal);
            System.out.println("headers >>> " + headers);

            UserSession userSession = UserSessionManager.getUserSession(stompPrincipal.getName());
            System.out.println("retrieved userSession : " + userSession);
            if (userSession == null)    {
                BlotterSubscriptionResponseMessage blotterSubscriptionResponseMessage = new BlotterSubscriptionResponseMessage();
                blotterSubscriptionResponseMessage.setSessionId(blotterSubscriptionRequestMessage.getSessionId());
                blotterSubscriptionResponseMessage.setType(blotterSubscriptionRequestMessage.getType());
                blotterSubscriptionResponseMessage.setStatus("NACK");
                blotterSubscriptionResponseMessage.setRejectText("Invalid sessionId " + blotterSubscriptionRequestMessage.getSessionId() + ". not found");
                stompEnhancedMessageSender.sendMessage(stompPrincipal.getName(),
                        ObjectMapperUtil.objectMapper.writeValueAsString(blotterSubscriptionResponseMessage), BLOTTER_SUBSCRIPTION_ENDPOINT);
                return;
            }
            userSession.setBlotterSubscriptionRequest(blotterSubscriptionRequestMessage);

            portfolioSubscriptionRequestHandler.onBlotterSubscriptionRequest(blotterSubscriptionRequestMessage, stompPrincipal);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @MessageMapping(BLOTTER_FILL_ENDPOINT)
    public void handleBlotterFillRequest(@Payload BlotterFillRequestMessage blotterFillRequestMessage,
                                                 StompPrincipal stompPrincipal, @Headers Map headers)   {

        try {
            System.out.println("blotterFillRequestMessage recvd : " + blotterFillRequestMessage);
            System.out.println("stompPrincipal >>> " + stompPrincipal);
            System.out.println("headers >>> " + headers);

            UserSession userSession = UserSessionManager.getUserSession(stompPrincipal.getName());
            System.out.println("retrieved userSession : " + userSession);
            if (userSession == null)    {
                BlotterFillResponseMessage blotterFillResponseMessage = new BlotterFillResponseMessage();
                blotterFillResponseMessage.setSessionId(blotterFillRequestMessage.getSessionId());
                blotterFillResponseMessage.setStartIndex(blotterFillRequestMessage.getStartIndex());
                blotterFillResponseMessage.setEndIndex(blotterFillRequestMessage.getEndIndex());
                blotterFillResponseMessage.setStatus("NACK");
                blotterFillResponseMessage.setRejectText("Invalid sessionId " + blotterFillResponseMessage.getSessionId() + ". not found");
                stompEnhancedMessageSender.sendMessage(stompPrincipal.getName(),
                        ObjectMapperUtil.objectMapper.writeValueAsString(blotterFillResponseMessage), BLOTTER_SUBSCRIPTION_ENDPOINT);
                return;
            }
            userSession.setBlotterFillRequest(blotterFillRequestMessage);

            portfolioSubscriptionRequestHandler.onBlotterFillRequest(blotterFillRequestMessage, stompPrincipal);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @MessageMapping(TRADE_RESOLUTION_ENDPOINT)
    public void handleTradeResolutionRequest(@Payload TradeResolutionRequestMessage tradeResolutionRequestMessage,
                                         StompPrincipal stompPrincipal, @Headers Map headers)   {

        try {
            System.out.println("tradeResolutionRequest recvd : " + tradeResolutionRequestMessage);
            System.out.println("stompPrincipal >>> " + stompPrincipal);
            System.out.println("headers >>> " + headers);

            UserSession userSession = UserSessionManager.getUserSession(stompPrincipal.getName());
            System.out.println("retrieved userSession : " + userSession);
            if (userSession == null)    {
                TradeResolutionResponseMessage tradeResolutionResponseMessage = new TradeResolutionResponseMessage();
                tradeResolutionResponseMessage.setSessionId(tradeResolutionRequestMessage.getSessionId());
                tradeResolutionResponseMessage.setTrades(new ArrayList<>());
                tradeResolutionResponseMessage.setStatus("NACK");
                tradeResolutionResponseMessage.setRejectText("Invalid sessionId " + tradeResolutionResponseMessage.getSessionId() + ". not found");
                stompEnhancedMessageSender.sendMessage(stompPrincipal.getName(),
                        ObjectMapperUtil.objectMapper.writeValueAsString(tradeResolutionResponseMessage), TRADE_RESOLUTION_ENDPOINT);
                return;
            }
            userSession.setTradeResolutionRequest(tradeResolutionRequestMessage);

            portfolioSubscriptionRequestHandler.onTradeResolutionRequest(tradeResolutionRequestMessage, stompPrincipal);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        System.out.println("Exception : " + exception);
        return exception.getMessage();
    }

}

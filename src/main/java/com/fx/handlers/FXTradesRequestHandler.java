package com.fx.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fx.domain.json.FXTradesSubscriptionRequest;
import com.fx.domain.json.FXTradesSubscriptionResponse;
import com.fx.service.StompEnhancedMessageSender;
import com.fx.utils.ObjectMapperUtil;
import com.fx.websocket.StompPrincipal;
import com.fx.websocket.endpoints.SubscriptionEndPoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FXTradesRequestHandler {

    @Autowired
    StompEnhancedMessageSender stompEnhancedMessageSender;

    public void onFXTradesSubscriptionRequest(FXTradesSubscriptionRequest fxTradesSubscriptionRequest, StompPrincipal stompPrincipal) throws JsonProcessingException {
        System.out.println("processing fxTradesRequest ...");

        // GRPC subscription here

        FXTradesSubscriptionResponse fxTradesSubscriptionResponse = new FXTradesSubscriptionResponse();

//        List<FxTrade> fxTrades = new ArrayList<>();
//        fxTrades.add(new FxTrade("111", "1", "EURUSD", "BUY", "EUR", "1000", "1234.56"));
//        fxTrades.add(new FxTrade("112", "1", "EURUSD", "BUY", "EUR", "2000", "2234.56"));
//        fxTradesSubscriptionResponse.setTrades(fxTrades);

        stompEnhancedMessageSender.sendMessage(stompPrincipal.getName(),
                ObjectMapperUtil.objectMapper.writeValueAsString(fxTradesSubscriptionResponse), SubscriptionEndPoints.FXTRADES_SUBSCRIPTION_ENDPOINT);
    }

}

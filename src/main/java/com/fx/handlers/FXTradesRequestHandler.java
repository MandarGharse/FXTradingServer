package com.fx.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fx.domain.FXTradesRequest;
import com.fx.domain.FXTradesResponse;
import com.fx.domain.FxTrade;
import com.fx.service.StompEnhancedMessageSender;
import com.fx.utils.ObjectMapperUtil;
import com.fx.websocket.StompPrincipal;
import com.fx.websocket.endpoints.SubscriptionEndPoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FXTradesRequestHandler {

    @Autowired
    StompEnhancedMessageSender stompEnhancedMessageSender;

    public void onData(FXTradesRequest fxTradesRequest, StompPrincipal stompPrincipal) throws JsonProcessingException {
        System.out.println("processing fxTradesRequest ...");

        FXTradesResponse fxTradesResponse = new FXTradesResponse();
        fxTradesResponse.setSessionId(fxTradesRequest.getSessionId());
        fxTradesResponse.setStartIndex(fxTradesRequest.getStartIndex());
        fxTradesResponse.setEndIndex(fxTradesRequest.getEndIndex());
        fxTradesResponse.setStatus("ACK");

        List<FxTrade> fxTrades = new ArrayList<>();
        fxTrades.add(new FxTrade("111", "1", "EURUSD", "BUY", "EUR", "1000", "1234.56"));
        fxTrades.add(new FxTrade("112", "1", "EURUSD", "BUY", "EUR", "2000", "2234.56"));
        fxTradesResponse.setTrades(fxTrades);

        stompEnhancedMessageSender.sendMessage(stompPrincipal.getName(),
                ObjectMapperUtil.objectMapper.writeValueAsString(fxTradesResponse), SubscriptionEndPoints.FXTRADES_ENDPOINT);
    }

}

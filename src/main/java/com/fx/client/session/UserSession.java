package com.fx.client.session;

import com.fx.client.websocket.StompPrincipal;
import com.fx.domain.json.BlotterFillRequestMessage;
import com.fx.domain.json.BlotterSubscriptionRequestMessage;
import com.fx.domain.json.TradeResolutionRequestMessage;
import com.fx.domain.json.TradesSubscriptionRequestMessage;
import com.fx.proto.messaging.TradeMessages;

public class UserSession {
    StompPrincipal stompPrincipal;
    TradesSubscriptionRequestMessage tradesSubscriptionRequestMessage;
    BlotterSubscriptionRequestMessage blotterSubscriptionRequestMessage;
    BlotterFillRequestMessage blotterFillRequestMessage;
    TradeResolutionRequestMessage tradeResolutionRequestMessage;

    // Any other UserSession related data

    public UserSession(StompPrincipal stompPrincipal) {
        this.stompPrincipal = stompPrincipal;
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "stompPrincipal=" + stompPrincipal +
                ", tradesSubscriptionRequestMessage=" + tradesSubscriptionRequestMessage +
                ", blotterSubscriptionRequestMessage=" + blotterSubscriptionRequestMessage +
                ", blotterFillRequestMessage=" + blotterFillRequestMessage +
                ", tradeResolutionRequestMessage=" + tradeResolutionRequestMessage +
                '}';
    }

    public void setFXTradesRequest(TradesSubscriptionRequestMessage tradesSubscriptionRequestMessage) {
        this.tradesSubscriptionRequestMessage = tradesSubscriptionRequestMessage;
    }

    public void setBlotterSubscriptionRequest(BlotterSubscriptionRequestMessage blotterSubscriptionRequestMessage) {
        this.blotterSubscriptionRequestMessage = blotterSubscriptionRequestMessage;
    }

    public void setBlotterFillRequest(BlotterFillRequestMessage blotterFillRequestMessage) {
        this.blotterFillRequestMessage = blotterFillRequestMessage;
    }

    public void setTradeResolutionRequest(TradeResolutionRequestMessage tradeResolutionRequestMessage) {
        this.tradeResolutionRequestMessage = tradeResolutionRequestMessage;
    }
}

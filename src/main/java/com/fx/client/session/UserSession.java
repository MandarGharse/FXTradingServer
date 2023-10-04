package com.fx.client.session;

import com.fx.client.websocket.StompPrincipal;
import com.fx.domain.json.*;
import com.fx.proto.messaging.TradeMessages;

public class UserSession {
    StompPrincipal stompPrincipal;
    TradesSubscriptionRequestMessage tradesSubscriptionRequestMessage;
    BlotterSubscriptionRequestMessage blotterSubscriptionRequestMessage;
    BlotterFillRequestMessage blotterFillRequestMessage;
    TradeResolutionRequestMessage tradeResolutionRequestMessage;

    PricingSubscriptionRequestMessage pricingSubscriptionRequestMessage;

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
                ", pricingSubscriptionRequestMessage=" + pricingSubscriptionRequestMessage +
                '}';
    }

    public void setPricingSubscriptionRequestMessage(PricingSubscriptionRequestMessage pricingSubscriptionRequestMessage) {
        this.pricingSubscriptionRequestMessage = pricingSubscriptionRequestMessage;
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

package com.fx.client.session;

import com.fx.client.websocket.StompPrincipal;
import com.fx.domain.json.BlotterFillRequestMessage;
import com.fx.domain.json.BlotterSubscriptionRequestMessage;
import com.fx.domain.json.TradesSubscriptionRequestMessage;

public class UserSession {
    StompPrincipal stompPrincipal;
    TradesSubscriptionRequestMessage tradesSubscriptionRequestMessage;
    BlotterSubscriptionRequestMessage blotterSubscriptionRequestMessage;
    BlotterFillRequestMessage blotterFillRequestMessage;

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

}

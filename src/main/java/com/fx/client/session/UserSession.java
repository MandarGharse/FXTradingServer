package com.fx.client.session;

import com.fx.client.websocket.StompPrincipal;
import com.fx.domain.json.FXTradesSubscriptionRequest;

public class UserSession {
    StompPrincipal stompPrincipal;
    FXTradesSubscriptionRequest fxTradesSubscriptionRequest;

    // Any other UserSession related data

    public UserSession(StompPrincipal stompPrincipal) {
        this.stompPrincipal = stompPrincipal;
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "stompPrincipal=" + stompPrincipal +
                ", fxTradesSubscriptionRequest=" + fxTradesSubscriptionRequest +
                '}';
    }

    public void setFXTradesRequest(FXTradesSubscriptionRequest fxTradesSubscriptionRequest) {
        this.fxTradesSubscriptionRequest = fxTradesSubscriptionRequest;
    }

}

package com.fx.session;

import com.fx.domain.FXTradesRequest;
import com.fx.websocket.StompPrincipal;

public class UserSession {
    StompPrincipal stompPrincipal;
    FXTradesRequest fxTradesRequest;

    // Any other UserSession related data

    public UserSession(StompPrincipal stompPrincipal) {
        this.stompPrincipal = stompPrincipal;
    }


    @Override
    public String toString() {
        return "UserSession{" +
                "stompPrincipal=" + stompPrincipal +
                '}';
    }

    public void setFXTradesRequest(FXTradesRequest fxTradesRequest) {
        this.fxTradesRequest = fxTradesRequest;
    }

}

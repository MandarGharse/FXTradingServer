package com.fx.websocket;

import java.security.Principal;

public class StompPrincipal implements Principal {

    private String loginId;
    private String name;
    private String sessionId;

    public StompPrincipal(String loginId, String name, String sessionId) {
        this.loginId = loginId;
        this.name = name;
        this.sessionId = sessionId;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "StompPrincipal{" +
                "loginId='" + loginId + '\'' +
                ", name='" + name + '\'' +
                ", sessionId=" + sessionId +
                '}';
    }
}

package com.fx.client.websocket;

import java.security.Principal;

public class StompPrincipal implements Principal {

    private String name;
    private String userId;

    public StompPrincipal(String name, String userId) {
        this.name = name;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "StompPrincipal{" +
                "name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}

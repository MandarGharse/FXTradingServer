package com.fx.session;

import com.fx.websocket.StompPrincipal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserSessionManager {

    static final Map<String, UserSession> userSessionMap = new ConcurrentHashMap();

    public static UserSession getUserSession(String sessionId) {
        return userSessionMap.get(sessionId);
    }

    public static void createUserSession(StompPrincipal stompPrincipal) {
        UserSession userSession = new UserSession(stompPrincipal);
        userSessionMap.put(stompPrincipal.getName(), userSession);
    }
}
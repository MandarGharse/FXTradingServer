package com.fx.service;

import com.fx.session.UserSessionManager;
import com.fx.websocket.StompPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Component
public class StompHandshakeHandler extends DefaultHandshakeHandler implements HandshakeInterceptor {

    @Override
    public Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        System.out.println("processing determineUser call ...");
        final ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) request;
        if (Objects.isNull(request) || Objects.isNull(servletServerHttpRequest))    {
            System.out.println("null request received in determineUser");
            return null;
        }

        final HttpServletRequest servletRequest = servletServerHttpRequest.getServletRequest();
        if (Objects.isNull(servletRequest))    {
            System.out.println("null servletRequest in determineUser");
            return super.determineUser(request, wsHandler, attributes);
        }

        HttpSession httpSession = servletRequest.getSession();
        if (Objects.isNull(httpSession))    {
            System.out.println("null httpSession in determineUser");
            return super.determineUser(request, wsHandler, attributes);
        }

        System.out.println("client with sessionId " + httpSession.getId() + " requesting connection from remote address " + request.getRemoteAddress() +
                " with headers " + request.getHeaders());
        System.out.println("attributes " + attributes + ", Principal " + request.getPrincipal());

        String sessionId = Objects.isNull(attributes.get("sessionId")) ? UUID.randomUUID().toString() : attributes.get("sessionId").toString();
        StompPrincipal stompPrincipal = new StompPrincipal(sessionId, sessionId);
        UserSessionManager.createUserSession(stompPrincipal);
        System.out.println("registered client with UserSessionManager with stompPrincipal " + stompPrincipal);

        return stompPrincipal;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}

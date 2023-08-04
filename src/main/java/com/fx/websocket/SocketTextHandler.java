package com.fx.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
public class SocketTextHandler extends TextWebSocketHandler {

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message){
        System.out.println("handleTextMessage called");
        String payload = message.getPayload();
//        JObject jsonObject = new JSONObject(payload);
        try {
            session.sendMessage(new TextMessage("Hi how may we help you?"));
        } catch (IOException e) {
            System.out.println("ERROOOOOOOOOOOOOOOOOOOOOOOOORRRRRRRRRRR!!!!!!!!!!");
            throw new RuntimeException(e);
        }
    }

}
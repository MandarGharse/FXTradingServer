package com.fx.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StompEnhancedMessageSender {

    private int maxChunkLength = 100000;
    private int maxNumberChunks = 5;

    @Autowired
    SimpMessageSendingOperations messagingTemplate;

    public boolean sendMessage(String sessionId, String message, String endpoint)   {
        endpoint = "/queue" + endpoint;

        List<String> parts = Arrays.asList(message.split("(?<=\\G.{" + maxChunkLength + "})"));

        if (parts.size() > maxNumberChunks) {
            System.out.println("failed to publish Response to session " + sessionId + " on endpoint " + endpoint + ". " +
                    "Message size exceeds " + maxNumberChunks*maxChunkLength + " bytes");
            return false;
        }

        Map<String, Object> headerComplete = new HashMap<>();
        long messageTimestamp = System.nanoTime();
        headerComplete.put("MESSAGE_TIMESTAMP", messageTimestamp);
        headerComplete.put("MESSAGE_TYPE", "COMPLETE");
        headerComplete.put("MESSAGE_CURRENTCOUNT", parts.size());
        headerComplete.put("MESSAGE_TOTALCOUNT", parts.size());

        if (parts.size() == 1)  {
            messagingTemplate.convertAndSendToUser(sessionId, endpoint, parts.get(0), headerComplete);
            System.out.println("published FINAL message to user with sessionId " + sessionId + " on endpoint " + endpoint);
            return true;
        }

        Map<String, Object> headerPartial = new HashMap<>();
        headerPartial.put("MESSAGE_TYPE", "PARTIAL");
        headerPartial.put("MESSAGE_TOTALCOUNT", parts.size());

        for (int count=0; count<parts.size()-1; count++)    {
            headerPartial.put("MESSAGE_TIMESTAMP", messageTimestamp);
            headerPartial.put("MESSAGE_CURRENTCOUNT", count+1);
            messagingTemplate.convertAndSendToUser(sessionId, endpoint, parts.get(count), headerPartial);
        }

        messagingTemplate.convertAndSendToUser(sessionId, endpoint, parts.get(parts.size()-1), headerComplete);

        System.out.println("published COMPLETE response to session " + sessionId + " on endpoint " + endpoint + ". count [" + parts.size() + "/" + parts.size() + "]");

        return true;
    }

}

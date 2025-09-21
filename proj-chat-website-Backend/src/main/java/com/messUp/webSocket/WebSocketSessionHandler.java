package com.messUp.webSocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketSessionHandler {

    private final SimpMessagingTemplate messagingTemplate;
    private final Map<String,String> userSessionMap = new ConcurrentHashMap<>();

    public WebSocketSessionHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void addUserToSession(String username, String sessionId) {
        userSessionMap.put(username, sessionId);
    }

    public String getSessionIdByUsername(String username) {
        return userSessionMap.get(username);
    }

    public void removeUserFromSession(String username) {
        userSessionMap.remove(username);
    }
}

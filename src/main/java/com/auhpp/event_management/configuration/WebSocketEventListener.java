package com.auhpp.event_management.configuration;

import com.auhpp.event_management.service.OnlineOfflineService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

@Component
public class WebSocketEventListener {
    private final OnlineOfflineService onlineOfflineService;

    public WebSocketEventListener(OnlineOfflineService onlineOfflineService) {
        this.onlineOfflineService = onlineOfflineService;
    }

    @EventListener
    public void handleConnectedEvent(SessionConnectedEvent event) {
        // get headers in info connect (SessionConnectedEvent)
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal user = event.getUser();
        String sessionId = accessor.getSessionId();
        if (user != null && sessionId != null) {
            onlineOfflineService.addOnlineUser(user.getName(), sessionId);
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal user = event.getUser();
        String sessionId = accessor.getSessionId();
        if (user != null && sessionId != null) {
            onlineOfflineService.removeOnlineUser(user.getName(), sessionId);
        }
    }
}

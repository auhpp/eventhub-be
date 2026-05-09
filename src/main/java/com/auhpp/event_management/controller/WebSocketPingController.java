package com.auhpp.event_management.controller;


import com.auhpp.event_management.service.OnlineOfflineService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebSocketPingController {
    OnlineOfflineService onlineOfflineService;

    @MessageMapping("/ping")
    public void handlePing(Principal user, SimpMessageHeaderAccessor headerAccessor) {
        if (user != null) {
            String sessionId = headerAccessor.getSessionId();

            // Dùng thẳng addOnlineUser để thay thế hoàn toàn extendExpire
            onlineOfflineService.addOnlineUser(user.getName(), sessionId);
        } else {
            log.warn("Nhận được PING nhưng không biết là ai (Principal bị NULL)!");
        }
    }
}

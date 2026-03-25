package com.auhpp.event_management.controller;


import com.auhpp.event_management.service.OnlineOfflineService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebSocketPingController {
    OnlineOfflineService onlineOfflineService;

    @MessageMapping("/ping")
    public void handlePing(Principal user) {
        if (user != null) {
            onlineOfflineService.extendExpire(user.getName());
        }
    }
}

package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.dto.response.NotificationResponse;
import com.auhpp.event_management.service.NotificationSenderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class NotificationSenderServiceImpl implements NotificationSenderService {

    SimpMessageSendingOperations simpMessageSendingOperations;

    @Override
    @Async("notificationTaskExecutor")
    public void pushNotificationToClients(NotificationResponse notificationResponse, List<Long> recipientIds) {
        for (Long recipientId : recipientIds) {
            simpMessageSendingOperations.convertAndSend(
                    "/topic/notification/" + recipientId,
                    notificationResponse
            );
        }
    }
}

package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.response.NotificationResponse;

import java.util.List;

public interface NotificationSenderService {
    void pushNotificationToClients(NotificationResponse notificationResponse, List<Long> recipientIds);
}

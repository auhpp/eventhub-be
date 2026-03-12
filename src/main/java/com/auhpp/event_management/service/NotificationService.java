package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.NotificationRequest;
import com.auhpp.event_management.dto.response.NotificationResponse;
import com.auhpp.event_management.dto.response.PageResponse;

public interface NotificationService {
    void createNotification(NotificationRequest notificationRequest);

    PageResponse<NotificationResponse> getNotifications(int page, int size);

    void deleteNotification(Long id);

    NotificationResponse seenNotification(Long id);

    Integer countUnseen();

}

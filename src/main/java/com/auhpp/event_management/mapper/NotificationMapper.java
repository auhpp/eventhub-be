package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.request.NotificationRequest;
import com.auhpp.event_management.dto.response.NotificationResponse;
import com.auhpp.event_management.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    Notification toNotification(NotificationRequest request);

    NotificationResponse toNotificationResponse(Notification notification);
}

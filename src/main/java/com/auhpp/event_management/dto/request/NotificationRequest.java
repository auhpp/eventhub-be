package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.NotificationSubjectType;
import com.auhpp.event_management.constant.NotificationTargetType;
import com.auhpp.event_management.constant.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    private NotificationType type;

    private List<Long> recipientIds;

    private NotificationSubjectType subjectType;

    private String subjectAvatar;

    private Long subjectId;

    private String subject;

    private NotificationTargetType targetType;

    private String targetAvatar;

    private Long targetId;

    private String target;

    private String message;
}

package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.NotificationSubjectType;
import com.auhpp.event_management.constant.NotificationTargetType;
import com.auhpp.event_management.constant.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private Long id;

    private String message;

    private NotificationType type;

    private NotificationSubjectType subjectType;

    private String subjectAvatar;

    private Long subjectId;

    private String subject;

    private NotificationTargetType targetType;

    private String targetAvatar;

    private Long targetId;

    private String target;

    private LocalDateTime createdAt;

    private boolean seen;
}

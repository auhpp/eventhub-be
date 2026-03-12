package com.auhpp.event_management.entity;

import com.auhpp.event_management.constant.NotificationSubjectType;
import com.auhpp.event_management.constant.NotificationTargetType;
import com.auhpp.event_management.constant.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    private NotificationSubjectType subjectType;

    private String subjectAvatar;

    private Long subjectId;

    private String subject;

    @Enumerated(EnumType.STRING)
    private NotificationTargetType targetType;

    private String targetAvatar;

    private Long targetId;

    private String target;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "notification", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true)
    private List<NotificationRecipient> notificationRecipients;
}

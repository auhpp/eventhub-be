package com.auhpp.event_management.entity;

import com.auhpp.event_management.constant.MessageStatus;
import com.auhpp.event_management.constant.MessageType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    @Column(columnDefinition = "TEXT")
    private String pathUrl;

    @Column(columnDefinition = "TEXT")
    private String publicId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageType type;

    private LocalDateTime seenAt;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToOne
    private Message replyMessage;

    @ManyToOne
    private ConversationMember sender;

    @ManyToOne
    private ConversationMember recipient;
}

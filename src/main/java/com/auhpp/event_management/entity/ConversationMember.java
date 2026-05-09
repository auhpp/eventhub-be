
package com.auhpp.event_management.entity;

import com.auhpp.event_management.constant.CommonStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConversationMember {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CommonStatus status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private AppUser appUser;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Conversation conversation;

    @OneToMany(mappedBy = "sender")
    private List<Message> sentMessages;

    @OneToMany(mappedBy = "recipient", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<Message> receivedMessages;
}

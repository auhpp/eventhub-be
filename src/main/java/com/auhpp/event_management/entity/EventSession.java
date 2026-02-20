package com.auhpp.event_management.entity;

import com.auhpp.event_management.constant.MeetingPlatform;
import com.auhpp.event_management.entity.converter.AttributeEncryptor;
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
public class EventSession {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    private LocalDateTime checkinStartTime;

    @Column(columnDefinition = "TEXT")
    @Convert(converter = AttributeEncryptor.class)
    private String meetingUrl;

    @Enumerated(EnumType.STRING)
    private MeetingPlatform meetingPlatform;

    @Column(columnDefinition = "TEXT")
    @Convert(converter = AttributeEncryptor.class)
    private String meetingPassword;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Event event;

    @OneToMany(mappedBy = "eventSession", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "eventSession")
    private List<EventImage> eventImages;

    public boolean isExpired() {
        return this.getEndDateTime().isBefore(LocalDateTime.now());
    }
}

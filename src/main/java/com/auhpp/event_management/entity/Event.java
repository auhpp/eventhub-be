package com.auhpp.event_management.entity;

import com.auhpp.event_management.constant.EventStatus;
import com.auhpp.event_management.constant.EventType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventType type;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(columnDefinition = "TEXT")
    private String location;

    @Column(columnDefinition = "geometry(Point,4326)")
    private Point locationCoordinates;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Double commissionRate;

    private Double commissionFixedPerTicket;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @Column(columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(columnDefinition = "TEXT")
    private String thumbnail;

    @Column(columnDefinition = "TEXT")
    private String thumbnailPublicId;

    @Column(columnDefinition = "TEXT")
    private String poster;

    @Column(columnDefinition = "TEXT")
    private String posterPublicId;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Category category;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private AppUser appUser;

    @OneToMany(mappedBy = "event")
    private List<EventSession> eventSessions;

    @OneToMany(mappedBy = "event")
    private List<EventStaff> eventStaffs;

    public boolean isExpired() {
        List<EventSession> eventSessions = this.getEventSessions();
        for (EventSession eventSession : eventSessions) {
            if (eventSession.getEndDateTime().isAfter(LocalDateTime.now())) {
                return false;
            }
        }
        return true;
    }
}

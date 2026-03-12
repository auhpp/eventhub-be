package com.auhpp.event_management.entity;

import com.auhpp.event_management.constant.EventSeriesStatus;
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
public class EventSeries {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String avatar;

    @Column(nullable = false)
    private String avatarPublicId;

    private String coverImage;

    private String coverImagePublicId;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Boolean hasPublic;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventSeriesStatus status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "eventSeries", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true)
    private List<EventSeriesFollower> eventSeriesFollowers;

    @OneToMany(mappedBy = "eventSeries")
    private List<Event> events;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private AppUser appUser;
}

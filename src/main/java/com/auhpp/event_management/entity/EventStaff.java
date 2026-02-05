package com.auhpp.event_management.entity;

import com.auhpp.event_management.constant.EventStaffStatus;
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
public class EventStaff {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventStaffStatus status;

    private String email;

    private String token;

    private LocalDateTime expiredAt;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(columnDefinition = "TEXT")
    private String rejectionMessage;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Role role;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Event event;

    @ManyToOne
    private AppUser appUser;

    @OneToMany(mappedBy = "eventStaff")
    private List<Attendee> attendees;
}

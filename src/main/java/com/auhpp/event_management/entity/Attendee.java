package com.auhpp.event_management.entity;

import com.auhpp.event_management.constant.AttendeeStatus;
import com.auhpp.event_management.constant.AttendeeType;
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
public class Attendee {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true)
    private String ticketCode;

    private LocalDateTime checkInAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendeeStatus status;

    @Column(nullable = false)
    private AttendeeType type;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Booking booking;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Ticket ticket;

    @ManyToOne
    private EventStaff eventStaff;
}

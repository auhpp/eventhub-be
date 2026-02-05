package com.auhpp.event_management.entity;

import com.auhpp.event_management.constant.AttendeeStatus;
import com.auhpp.event_management.constant.AttendeeType;
import com.auhpp.event_management.constant.SourceType;
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
public class Attendee {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true)
    private String ticketCode;

    private LocalDateTime checkInAt;

    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendeeStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AttendeeType type;

    @Enumerated(EnumType.STRING)
    private SourceType sourceType;

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

    @ManyToOne
    private AppUser owner;

    @OneToMany(mappedBy = "attendee")
    private List<AttendeeTicketGift> attendeeTicketGifts;


}

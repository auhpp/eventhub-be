package com.auhpp.event_management.entity;

import com.auhpp.event_management.constant.TicketGiftStatus;
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
public class TicketGift {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TicketGiftStatus status;

    private LocalDateTime expiredAt;

    @Column(columnDefinition = "TEXT")
    private String rejectionMessage;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private AppUser sender;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private AppUser receiver;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Booking booking;

    @OneToMany(mappedBy = "ticketGift", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<AttendeeTicketGift> attendeeTicketGifts;


}

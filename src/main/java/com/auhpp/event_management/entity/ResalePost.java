package com.auhpp.event_management.entity;

import com.auhpp.event_management.constant.ResalePostStatus;
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
public class ResalePost {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private Double pricePerTicket;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ResalePostStatus status;

    @Column(nullable = false)
    private Boolean hasRetail;

    @Column(columnDefinition = "TEXT")
    private String rejectionMessage;

    private Double commissionRate;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "resalePost")
    private List<Booking> bookings;

    @OneToMany(mappedBy = "resalePost", cascade = {CascadeType.MERGE})
    private List<Attendee> attendees;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private AppUser appUser;
}

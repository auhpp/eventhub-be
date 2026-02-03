package com.auhpp.event_management.entity;

import com.auhpp.event_management.constant.AttendeeType;
import com.auhpp.event_management.constant.BookingStatus;
import com.auhpp.event_management.constant.WalletType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double totalAmount;

    private Double discountAmount;

    private Double finalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    private String transactionId;

    private String customerName;

    private String customerEmail;

    private String customerPhone;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Enumerated(EnumType.STRING)
    private WalletType walletType;

    private LocalDateTime expiredAt;

    @Column(nullable = false)
    private AttendeeType type;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Attendee> attendees;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private AppUser appUser;

    @OneToMany(mappedBy = "booking")
    private List<EventInvitation> eventInvitations;
}
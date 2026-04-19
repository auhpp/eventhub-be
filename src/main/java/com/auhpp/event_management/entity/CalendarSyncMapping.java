package com.auhpp.event_management.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarSyncMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String googleEventId;

    @ManyToOne
    @JoinColumn(nullable = false)
    private AppUser appUser;

    @ManyToOne
    @JoinColumn(nullable = false)
    private EventSession eventSession;


}

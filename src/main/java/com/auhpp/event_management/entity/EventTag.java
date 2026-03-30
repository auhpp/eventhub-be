package com.auhpp.event_management.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventTag {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Event event;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Tag tag;
}

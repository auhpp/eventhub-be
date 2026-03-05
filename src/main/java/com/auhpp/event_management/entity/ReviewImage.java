package com.auhpp.event_management.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewImage {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String imageUrl;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String imagePublicId;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Review review;
}

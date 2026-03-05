package com.auhpp.event_management.entity;

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
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String comment;

    private int editCount;

    @Column(nullable = false)
    private Integer rating;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private EventSession eventSession;

    @OneToOne(optional = false)
    @JoinColumn(nullable = false)
    private Attendee attendee;

    @OneToMany(mappedBy = "review", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<ReviewImage> reviewImages;
}

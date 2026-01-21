package com.auhpp.event_management.entity;

import com.auhpp.event_management.constant.SocialType;
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
public class SocialLink {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String urlLink;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SocialType type;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private AppUser appUser;
}

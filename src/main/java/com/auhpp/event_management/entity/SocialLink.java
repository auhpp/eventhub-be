package com.auhpp.event_management.entity;

import com.auhpp.event_management.constant.SocialType;
import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne
    private AppUser appUser;
}

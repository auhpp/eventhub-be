package com.auhpp.event_management.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String fullName;

    private String password;

    private String phoneNumber;

    private String avatar;

    @Column(length = 1000)
    private String biography;

    @Column(unique = true, nullable = false)
    private String email;

    private Boolean isOnline;

    private LocalDateTime lastSeen;

    private Boolean status;

    private String authProvider;

    private String providerId;

    @Column(length = 300)
    private String refreshToken;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "appUser")
    private List<SocialLink> socialLinks;

    @OneToMany(mappedBy = "appUser")
    private List<OrganizerRegistration> organizerRegistrations;
}
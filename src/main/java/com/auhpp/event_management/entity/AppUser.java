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
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String fullName;

    private String password;

    private String phoneNumber;

    @Column(columnDefinition = "TEXT")
    private String avatar;

    @Column(columnDefinition = "TEXT")
    private String avatarPublicId;

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

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "appUser")
    private List<SocialLink> socialLinks;

    @OneToMany(mappedBy = "appUser")
    private List<OrganizerRegistration> organizerRegistrations;

    @OneToMany(mappedBy = "appUser")
    private List<Event> events;

    @OneToMany(mappedBy = "appUser")
    private List<EventStaff> eventStaffs;

    @OneToMany(mappedBy = "appUser")
    private List<Booking> bookings;

    @OneToMany(mappedBy = "appUser")
    private List<EventInvitation> eventInvitations;

    @OneToMany(mappedBy = "sender")
    private List<TicketGift> sendTicketGifts;

    @OneToMany(mappedBy = "receiver")
    private List<TicketGift> receiveTicketGifts;

    @OneToMany(mappedBy = "owner")
    private List<Attendee> attendees;

    @OneToMany(mappedBy = "appUser")
    private List<EventSeriesFollower> eventSeriesFollowers;

    @OneToMany(mappedBy = "appUser")
    private List<EventSeries> eventSeries;

    @OneToMany(mappedBy = "appUser")
    private List<CategoryFollower> categoryFollowers;

    @OneToMany(mappedBy = "appUser")
    private List<NotificationRecipient> notificationRecipients;

    @OneToMany(mappedBy = "appUser")
    private List<ResalePost> resalePosts;

    @OneToMany(mappedBy = "appUser")
    private List<ConversationMember> conversationMembers;

    @OneToMany(mappedBy = "appUser")
    private List<Favorite> favorites;

    @OneToMany(mappedBy = "appUser")
    private List<Question> questions;

    @OneToMany(mappedBy = "appUser")
    private List<UpvoteQuestion> upvoteQuestions;

    @OneToMany(mappedBy = "appUser")
    private List<UserNote> userNotes;

    @OneToMany(mappedBy = "appUser",
            cascade = {CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST},
            orphanRemoval = true)
    private List<UserTag> userTags;
}
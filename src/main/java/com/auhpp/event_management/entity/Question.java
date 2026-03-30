package com.auhpp.event_management.entity;


import com.auhpp.event_management.constant.QuestionStatus;
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
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestionStatus status;

    private Boolean hasAnonymous;

    private Boolean hasPin;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private EventSession eventSession;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private AppUser appUser;

    @OneToMany(mappedBy = "question", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    List<UpvoteQuestion> upvoteQuestions;

    @OneToMany(mappedBy = "question")
    List<UserNote> userNotes;
}

package com.auhpp.event_management.entity;

import com.auhpp.event_management.constant.ProcessStatus;
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
public class EventImage {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String imageUrl;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String publicId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProcessStatus processStatus;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    private EventSession eventSession;

    @ManyToOne
    private Event event;

    @OneToMany(mappedBy = "eventImage")
    private List<FaceData> faceDataList;
}

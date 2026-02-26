package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.EventStatus;
import com.auhpp.event_management.constant.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {
    private Long id;

    private String name;

    private EventType type;

    private String location;

    private String address;

    private PointResponse locationCoordinates;

    private String description;

    private EventStatus status;

    private String rejectionReason;

    private String thumbnail;

    private String poster;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private CategoryResponse category;

    private List<EventSessionResponse> eventSessions;

    private UserResponse appUser;

    private boolean hasPhotos;
}

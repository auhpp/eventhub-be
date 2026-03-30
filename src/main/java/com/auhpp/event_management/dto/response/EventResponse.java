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

    private Double commissionRate;

    private Double commissionFixedPerTicket;

    private String poster;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private CategoryResponse category;

    private List<EventSessionResponse> eventSessions;

    private List<TagResponse> eventTags;

    private UserResponse appUser;

    private EventSeriesResponse eventSeries;

    private boolean hasPhotos;
}

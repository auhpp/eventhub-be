package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.EventStatus;
import com.auhpp.event_management.constant.EventType;
import com.auhpp.event_management.constant.MeetingPlatform;
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

    private PointResponse locationCoordinates;

    private String description;

    private String meetingUrl;

    private MeetingPlatform meetingPlatform;

    private EventStatus status;

    private String rejectionReason;

    private String thumbnail;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private CategoryResponse category;

    private List<EventSessionResponse> eventSessions;

    private UserResponse appUser;
}

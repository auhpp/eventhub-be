package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.EventType;
import com.auhpp.event_management.constant.MeetingPlatform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventSummaryResponse {
    private Long id;

    private String name;

    private EventType type;

    private String location;

    private String thumbnail;

    private PointResponse locationCoordinates;

    private String meetingUrl;

    private MeetingPlatform meetingPlatform;

    private CategoryResponse category;

}

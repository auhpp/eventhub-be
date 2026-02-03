package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventBasicResponse {
    private Long id;

    private String name;

    private EventType type;

    private String location;

    private String thumbnail;

    private PointResponse locationCoordinates;

    private String poster;

    private CategoryResponse category;

}

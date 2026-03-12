package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.EventSeriesStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventSeriesResponse {
    private Long id;

    private String avatar;

    private String coverImage;

    private String name;

    private String description;

    private Boolean hasPublic;

    private EventSeriesStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private UserBasicResponse appUser;

}

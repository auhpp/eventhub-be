package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.MeetingPlatform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventSessionBasicResponse {
    private Long id;

    private String name;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private LocalDateTime checkinStartTime;

    private MeetingPlatform meetingPlatform;

    private String meetingUrl;

    private String meetingPassword;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.MeetingPlatform;
import com.auhpp.event_management.validation.annotation.ValidUpdateEventSession;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidUpdateEventSession
public class EventSessionUpdateRequest {
    private LocalDateTime checkinStartTime;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private String meetingUrl;

    private MeetingPlatform meetingPlatform;

    private String meetingPassword;

}

package com.auhpp.event_management.dto.response;

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
public class EventSessionResponse {
    private Long id;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private LocalDateTime checkinStartTime;

    private MeetingPlatform meetingPlatform;

    private String meetingUrl;

    private String meetingPassword;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<TicketResponse> tickets;
}

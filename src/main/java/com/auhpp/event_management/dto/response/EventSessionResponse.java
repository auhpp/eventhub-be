package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.EventSessionStatus;
import com.auhpp.event_management.constant.MeetingPlatform;
import com.auhpp.event_management.constant.QAStatus;
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

    private EventSessionStatus status;

    private QAStatus qaStatus;

    private Boolean allowAnonymous;

    private Boolean requireModerationQuestion;

    private String meetingPassword;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<TicketResponse> tickets;
}

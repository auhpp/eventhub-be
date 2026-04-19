package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.ActionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInLogSearchRequest {
    private String ticketCode;
    private Long attendeeId;

    private ActionType actionType;
    private Long eventStaffId;

    private LocalDateTime fromTime;
    private LocalDateTime toTime;
}

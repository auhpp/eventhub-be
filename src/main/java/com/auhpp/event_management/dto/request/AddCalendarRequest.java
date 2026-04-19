package com.auhpp.event_management.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddCalendarRequest {
    private String authCode;
    private Long userId;
    private Long eventSessionId;

    private Integer ticketCount;
    private Long bookingId;
}

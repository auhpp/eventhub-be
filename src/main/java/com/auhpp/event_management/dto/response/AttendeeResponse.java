package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.AttendeeStatus;
import com.auhpp.event_management.constant.AttendeeType;
import com.auhpp.event_management.constant.SourceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendeeResponse {
    private Long id;

    private String ticketCode;

    private LocalDateTime checkInAt;

    private AttendeeStatus status;

    private AttendeeType type;

    private SourceType sourceType;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private UserBasicResponse owner;

    private BookingSummaryResponse booking;

    private UserResponse user;

    private EventSessionResponse eventSession;

    private TicketResponse ticket;

    private EventBasicResponse event;


}

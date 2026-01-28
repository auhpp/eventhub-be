package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.AttendeeStatus;
import com.auhpp.event_management.constant.AttendeeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendeeBasicResponse {
    private Long id;

    private String ticketCode;

    private AttendeeType type;

    private AttendeeStatus status;

    private TicketBasicResponse ticket;

}

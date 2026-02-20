package com.auhpp.event_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketCheckInResponse {
    private String name;
    private Integer totalQuantity;
    private Integer soldQuantity;
    private Integer checkInQuantity;
    private Integer guestTotalQuantity;
    private Integer guestInvitedQuantity;
    private Integer guestCheckedInQuantity;
}

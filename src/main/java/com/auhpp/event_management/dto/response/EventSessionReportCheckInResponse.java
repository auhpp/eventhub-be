package com.auhpp.event_management.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventSessionReportCheckInResponse {
    private Integer totalQuantity;
    private Integer soldQuantity;
    private Integer checkedInQuantity;
    private Integer outsideQuantity;
    private Integer guestTotalQuantity;
    private Integer guestInvitedQuantity;
    private Integer guestCheckedInQuantity;
    private Integer guestOutsideQuantity;
    private List<TicketCheckInResponse> ticketCheckIns;
}

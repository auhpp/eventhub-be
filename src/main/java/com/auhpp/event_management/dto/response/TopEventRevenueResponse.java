package com.auhpp.event_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopEventRevenueResponse {
    private Double totalCommission;

   private Long eventId;

    private String eventName;

    private String organizerName;

    private Long totalTicketsSold;

}

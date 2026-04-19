package com.auhpp.event_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventOverviewStatsResponse {
    private Double totalRevenue;
    private Double totalResaleRevenue;
    private Double maxPotentialRevenue;
    private Double revenuePercentage;
    private Double discountAmount;
    private Double voucherRevenue;
    private Double totalFee;
    private Double totalFeeFromResale;

    private Integer totalTicketsSold;
    private Integer totalCapacity;
    private Double ticketsSoldPercentage;
}

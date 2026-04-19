package com.auhpp.event_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizerKpiReportResponse {
    private double totalRevenue;

    private double totalRevenueAfterVoucher;

    private double totalFee;

    private int activeEvents;

    private int upcomingEvents;

    private int pastEvents;

    private int totalTicketSold;

    private int totalTicketAttendees;

    private int totalGuestAttendees;

    private int totalGuestsInvited;

    private double totalRating;

    private int totalReviews;
}

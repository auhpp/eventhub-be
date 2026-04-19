package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.StatsFilterRequest;
import com.auhpp.event_management.dto.response.EventOverviewStatsResponse;
import com.auhpp.event_management.dto.response.OrganizerReviewSummaryResponse;
import com.auhpp.event_management.dto.response.RevenueChartResponse;
import com.auhpp.event_management.dto.response.TopEventRevenueResponse;

import java.util.List;

public interface StatsService {
    EventOverviewStatsResponse getEventStats(StatsFilterRequest request);

    List<TopEventRevenueResponse> getTopEventRevenue(
            StatsFilterRequest request);


    List<RevenueChartResponse> getVoucherRevenueWithTimeLabel(
            StatsFilterRequest request);

    OrganizerReviewSummaryResponse getOrganizerReviewSummary(
            StatsFilterRequest request
    );
}

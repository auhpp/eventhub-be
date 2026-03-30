package com.auhpp.event_management.service;

import com.auhpp.event_management.constant.EventStatus;
import com.auhpp.event_management.constant.RevenueSource;
import com.auhpp.event_management.constant.RoleName;
import com.auhpp.event_management.dto.request.DateRangeFilterRequest;
import com.auhpp.event_management.dto.request.PaginationFilterRequest;
import com.auhpp.event_management.dto.response.*;

import java.util.List;

public interface AdminStatsService {
    KpiOverviewResponse getKpiOverview(DateRangeFilterRequest request);

    List<RevenueChartResponse> getRevenueCharts(DateRangeFilterRequest request, RevenueSource revenueSource);

    List<TopEventRevenueResponse> getTopEventRevenue(DateRangeFilterRequest request,
                                                     PaginationFilterRequest paginationFilterRequest);

    List<TopOrganizerResponse> getTopOrganizer(DateRangeFilterRequest request,
                                               PaginationFilterRequest paginationFilterRequest);

    List<CategoryDistributionResponse> getCategoryDistribution(DateRangeFilterRequest request,
                                                               EventStatus status);

    EventApprovalStatResponse getEventApprovalStat(DateRangeFilterRequest request);

    List<TopResaleEventResponse> getTopResaleEvent(DateRangeFilterRequest request,
                                                   PaginationFilterRequest paginationFilterRequest);


    List<UserGrowthResponse> getUserGrowthResponse(DateRangeFilterRequest request, RoleName roleName);

    ResaleOverviewResponse getResaleOverviewResponse(DateRangeFilterRequest request, Long eventId);
}

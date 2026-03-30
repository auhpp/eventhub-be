package com.auhpp.event_management.repository.custom;

import com.auhpp.event_management.constant.SourceType;
import com.auhpp.event_management.dto.request.DateRangeFilterRequest;
import com.auhpp.event_management.dto.request.PaginationFilterRequest;
import com.auhpp.event_management.dto.response.RevenueChartResponse;
import com.auhpp.event_management.dto.response.TopEventRevenueResponse;
import com.auhpp.event_management.dto.response.TopOrganizerResponse;

import java.util.List;

public interface AttendeeCustomRepository {
    List<RevenueChartResponse> getCommissionWithTimeLabel(
            Long eventSessionId, SourceType sourceType, DateRangeFilterRequest dateRangeFilterRequest);

    List<TopEventRevenueResponse> getTopEventRevenue(
            DateRangeFilterRequest request, PaginationFilterRequest paginationFilterRequest);

    List<TopOrganizerResponse> getTopOrganizerRevenue(
            DateRangeFilterRequest request, PaginationFilterRequest paginationFilterRequest);
}

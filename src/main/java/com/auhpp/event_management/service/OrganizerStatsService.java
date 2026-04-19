package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.DateRangeFilterRequest;
import com.auhpp.event_management.dto.response.OrganizerKpiReportResponse;
import com.auhpp.event_management.dto.response.OrganizerReviewSummaryResponse;

public interface OrganizerStatsService {
    OrganizerKpiReportResponse getOrganizerKpiReport(Long organizerId, DateRangeFilterRequest request);




}

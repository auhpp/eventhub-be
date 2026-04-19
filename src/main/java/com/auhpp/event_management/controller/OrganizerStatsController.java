package com.auhpp.event_management.controller;

import com.auhpp.event_management.dto.request.DateRangeFilterRequest;
import com.auhpp.event_management.dto.request.PaginationFilterRequest;
import com.auhpp.event_management.dto.response.OrganizerKpiReportResponse;
import com.auhpp.event_management.dto.response.OrganizerReviewSummaryResponse;
import com.auhpp.event_management.dto.response.RevenueChartResponse;
import com.auhpp.event_management.dto.response.TopEventRevenueResponse;
import com.auhpp.event_management.service.OrganizerStatsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/organizer/reports")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrganizerStatsController {
    OrganizerStatsService organizerStatsService;

    @GetMapping("/kpis/{organizerId}")
    @PreAuthorize(value = "hasRole('ORGANIZER')")
    public ResponseEntity<OrganizerKpiReportResponse> getOrganizerKpiReport(
            @PathVariable("organizerId") Long organizerId,
            DateRangeFilterRequest request
    ) {
        OrganizerKpiReportResponse response = organizerStatsService.getOrganizerKpiReport(organizerId, request);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

}

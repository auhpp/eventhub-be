package com.auhpp.event_management.controller;

import com.auhpp.event_management.dto.request.StatsFilterRequest;
import com.auhpp.event_management.dto.response.EventOverviewStatsResponse;
import com.auhpp.event_management.dto.response.OrganizerReviewSummaryResponse;
import com.auhpp.event_management.dto.response.RevenueChartResponse;
import com.auhpp.event_management.dto.response.TopEventRevenueResponse;
import com.auhpp.event_management.service.StatsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stats")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatsController {
    StatsService statsService;

    @PostMapping("/overview")
    public ResponseEntity<EventOverviewStatsResponse> getEventStats(
            @RequestBody StatsFilterRequest request
    ) {
        EventOverviewStatsResponse res = statsService.getEventStats(request);
        return ResponseEntity
                .status(HttpStatus.OK).body(res);
    }

    @PostMapping("/revenue/top-events")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    public ResponseEntity<List<TopEventRevenueResponse>> getTopEventRevenue(
            @RequestBody StatsFilterRequest request
    ) {
        List<TopEventRevenueResponse> response = statsService.getTopEventRevenue(request);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @PostMapping("/revenue/chart")
    public ResponseEntity<List<RevenueChartResponse>> getVoucherRevenueWithTimeLabel(
            @RequestBody StatsFilterRequest request
    ) {
        List<RevenueChartResponse> response = statsService.getVoucherRevenueWithTimeLabel(request);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }


    @PostMapping("/reviews-summary")
    public ResponseEntity<OrganizerReviewSummaryResponse> getReviewSummary(
            @RequestBody StatsFilterRequest request

    ) {
        OrganizerReviewSummaryResponse response = statsService.getOrganizerReviewSummary(
                request);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }
}

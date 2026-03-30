package com.auhpp.event_management.controller;

import com.auhpp.event_management.constant.EventStatus;
import com.auhpp.event_management.constant.RevenueSource;
import com.auhpp.event_management.constant.RoleName;
import com.auhpp.event_management.dto.request.DateRangeFilterRequest;
import com.auhpp.event_management.dto.request.PaginationFilterRequest;
import com.auhpp.event_management.dto.response.*;
import com.auhpp.event_management.service.AdminStatsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/reports")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminStatsController {
    AdminStatsService adminStatsService;

    @GetMapping("/kpis")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<KpiOverviewResponse> getKpiOverview(
            DateRangeFilterRequest request
    ) {
        KpiOverviewResponse response = adminStatsService.getKpiOverview(request);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @GetMapping("/revenue/chart")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RevenueChartResponse>> getRevenueCharts(
            DateRangeFilterRequest request,
            @RequestParam("revenueSource") RevenueSource revenueSource
    ) {
        List<RevenueChartResponse> response = adminStatsService.getRevenueCharts(request, revenueSource);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @GetMapping("/revenue/top-events")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TopEventRevenueResponse>> getTopEventRevenue(
            DateRangeFilterRequest request,
            PaginationFilterRequest paginationFilterRequest
    ) {
        List<TopEventRevenueResponse> response = adminStatsService.getTopEventRevenue(request, paginationFilterRequest);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @GetMapping("/revenue/top-organizers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TopOrganizerResponse>> getTopOrganizer(
            DateRangeFilterRequest request,
            PaginationFilterRequest paginationFilterRequest
    ) {
        List<TopOrganizerResponse> response = adminStatsService.getTopOrganizer(request, paginationFilterRequest);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @GetMapping("/events/category-distribution")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CategoryDistributionResponse>> getCategoryDistribution(
            DateRangeFilterRequest request,
            EventStatus status
    ) {
        List<CategoryDistributionResponse> response = adminStatsService.getCategoryDistribution(request, status);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @GetMapping("/events/approval-stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventApprovalStatResponse> getEventApprovalStat(
            DateRangeFilterRequest request) {
        EventApprovalStatResponse response = adminStatsService.getEventApprovalStat(request);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @GetMapping("/resales/overview")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResaleOverviewResponse> getResaleOverviewResponse(
            DateRangeFilterRequest request,
            Long eventId) {
        ResaleOverviewResponse response = adminStatsService.getResaleOverviewResponse(request, eventId);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @GetMapping("/resales/top-events")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TopResaleEventResponse>> getTopResaleEvent(
            DateRangeFilterRequest request,
            PaginationFilterRequest paginationFilterRequest) {
        List<TopResaleEventResponse> response = adminStatsService.getTopResaleEvent(request, paginationFilterRequest);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @GetMapping("/users/growth-chart")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserGrowthResponse>> getUserGrowthResponse(
            DateRangeFilterRequest request,
            RoleName roleName) {
        List<UserGrowthResponse> response = adminStatsService.getUserGrowthResponse(request, roleName);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }
}

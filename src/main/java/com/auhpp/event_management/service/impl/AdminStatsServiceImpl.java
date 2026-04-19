package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.*;
import com.auhpp.event_management.dto.request.DateRangeFilterRequest;
import com.auhpp.event_management.dto.request.PaginationFilterRequest;
import com.auhpp.event_management.dto.request.StatsFilterRequest;
import com.auhpp.event_management.dto.response.*;
import com.auhpp.event_management.repository.*;
import com.auhpp.event_management.service.AdminStatsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AdminStatsServiceImpl implements AdminStatsService {

    BookingRepository bookingRepository;
    AttendeeRepository attendeeRepository;
    ResalePostRepository resalePostRepository;
    EventRepository eventRepository;
    AppUserRepository appUserRepository;

    @Override
    public KpiOverviewResponse getKpiOverview(DateRangeFilterRequest request) {
        Double totalFmv = bookingRepository.getVoucherRevenue(null, null, null,
                BookingType.BUY, request.getStartDate(),
                request.getEndDate());

        Double commissionFromEvents = attendeeRepository.getCommissionFromEvents(
                null, null, null, List.of(
                        SourceType.PURCHASE
                ), request.getStartDate(), request.getEndDate());

        Double commissionFomResales = attendeeRepository.getCommissionFromResales(null,
                null, List.of(
                        SourceType.RESALE
                ), request.getStartDate(), request.getEndDate());

        Integer activeEventsCount = eventRepository.countEvent(null, null, List.of(EventStatus.APPROVED),
                request.getStartDate(), request.getEndDate());

        Integer pendingEventsCount = eventRepository.countEvent(null, null, List.of(EventStatus.PENDING),
                request.getStartDate(), request.getEndDate());

        Integer rejectEventsCount = eventRepository.countEvent(null, null, List.of(EventStatus.REJECTED),
                request.getStartDate(), request.getEndDate());

        Integer cancelEventsCount = eventRepository.countEvent(null, null, List.of(EventStatus.CANCELLED),
                request.getStartDate(), request.getEndDate());

        Integer pendingResalesCount = resalePostRepository.countResalePost(
                List.of(ResalePostStatus.PENDING),
                request.getStartDate(), request.getEndDate());

        Integer newUsersCount = appUserRepository.countUser(List.of(true), request.getStartDate(),
                request.getEndDate());

        return KpiOverviewResponse.builder()
                .totalFmv(totalFmv)
                .commissionFromEvents(commissionFromEvents)
                .commissionFomResales(commissionFomResales)
                .activeEventsCount(activeEventsCount)
                .pendingEventsCount(pendingEventsCount)
                .rejectEventsCount(rejectEventsCount)
                .cancelEventsCount(cancelEventsCount)
                .pendingResalesCount(pendingResalesCount)
                .newUsersCount(newUsersCount)
                .build();
    }

    @Override
    public List<RevenueChartResponse> getRevenueCharts(StatsFilterRequest request, RevenueSource revenueSource) {
        BookingType bookingType;
        SourceType sourceType;
        if (revenueSource.equals(RevenueSource.RESALE)) {
            bookingType = BookingType.RESALE;
            sourceType = SourceType.RESALE;
        } else {
            bookingType = BookingType.BUY;
            sourceType = SourceType.PURCHASE;
        }
        List<RevenueChartResponse> gmvRes = bookingRepository.getVoucherRevenueWithTimeLabel(request, bookingType);
        List<RevenueChartResponse> commissionRes = attendeeRepository.getCommissionWithTimeLabel(null,
                sourceType, request.getDateRangeFilter());
        Map<String, Double> mapCommission = new HashMap<>();
        for (RevenueChartResponse commission : commissionRes) {
            mapCommission.put(commission.getTimeLabel(), commission.getCommission());
        }
        for (RevenueChartResponse gmv : gmvRes) {
            if (mapCommission.containsKey(gmv.getTimeLabel())) {
                gmv.setCommission(mapCommission.get(gmv.getTimeLabel()));
            }
        }
        return gmvRes;
    }

    @Override
    public List<TopOrganizerResponse> getTopOrganizer(
            DateRangeFilterRequest request, PaginationFilterRequest paginationFilterRequest) {
        return attendeeRepository.getTopOrganizerRevenue(request, paginationFilterRequest);
    }

    @Override
    public List<CategoryDistributionResponse> getCategoryDistribution(DateRangeFilterRequest request, EventStatus status) {
        return eventRepository.getCategoryDistribution(request, status);
    }

    @Override
    public EventApprovalStatResponse getEventApprovalStat(DateRangeFilterRequest request) {

        return eventRepository.getEventApprovalStatResponse(request);
    }

    @Override
    public List<TopResaleEventResponse> getTopResaleEvent(DateRangeFilterRequest request,
                                                          PaginationFilterRequest paginationFilterRequest) {

        return resalePostRepository.getTopResaleEvent(request, paginationFilterRequest);
    }

    @Override
    public List<UserGrowthResponse> getUserGrowthResponse(DateRangeFilterRequest request, RoleName roleName) {
        return appUserRepository.getUserGrowthResponse(request, roleName);
    }

    @Override
    public ResaleOverviewResponse getResaleOverviewResponse(DateRangeFilterRequest request, Long eventId) {
        return resalePostRepository.getResaleOverviewResponse(request, eventId);
    }

}

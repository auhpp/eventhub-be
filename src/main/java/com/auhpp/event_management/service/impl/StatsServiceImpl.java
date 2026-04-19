package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.BookingType;
import com.auhpp.event_management.constant.SourceType;
import com.auhpp.event_management.dto.request.StatsFilterRequest;
import com.auhpp.event_management.dto.response.*;
import com.auhpp.event_management.repository.AttendeeRepository;
import com.auhpp.event_management.repository.BookingRepository;
import com.auhpp.event_management.repository.EventSessionRepository;
import com.auhpp.event_management.repository.ReviewRepository;
import com.auhpp.event_management.service.StatsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    BookingRepository bookingRepository;
    EventSessionRepository eventSessionRepository;
    AttendeeRepository attendeeRepository;
    ReviewRepository reviewRepository;

    @Override
    public EventOverviewStatsResponse getEventStats(StatsFilterRequest request) {
        Double totalRevenue = bookingRepository.getTotalRevenue(request.getEventSeriesId(),
                request.getOrganizerId(), request.getEventSessionId(), BookingType.BUY,
                request.getDateRangeFilter().getStartDate(),
                request.getDateRangeFilter().getEndDate());
        Double totalRevenueResale = bookingRepository.getTotalRevenue(
                request.getEventSeriesId(), request.getOrganizerId(), request.getEventSessionId(), BookingType.RESALE,
                request.getDateRangeFilter().getStartDate(),
                request.getDateRangeFilter().getEndDate());

        Double maxPotentialRevenue = eventSessionRepository.getMaxPotentialRevenue(request.getEventSeriesId(),
                request.getEventSessionId());
        Double voucherRevenue = bookingRepository.getVoucherRevenue(
                request.getOrganizerId(),
                request.getEventSeriesId(),
                request.getEventSessionId(),
                BookingType.BUY,
                request.getDateRangeFilter().getStartDate(),
                request.getDateRangeFilter().getEndDate());
        Double discountAmount = eventSessionRepository.getDiscountAmount(request.getEventSeriesId(),
                request.getEventSessionId(),
                request.getDateRangeFilter().getStartDate(),
                request.getDateRangeFilter().getEndDate());
        Double totalFee = attendeeRepository.getCommissionFromEvents(
                null,
                request.getEventSessionId(),
                request.getEventSeriesId(),
                List.of(SourceType.PURCHASE)
                , request.getDateRangeFilter().getStartDate(),
                request.getDateRangeFilter().getEndDate());
        Double totalFeeResale = attendeeRepository.getCommissionFromResales(
                request.getEventSessionId(),
                request.getEventSeriesId(),
                List.of(SourceType.RESALE)
                , request.getDateRangeFilter().getStartDate(),
                request.getDateRangeFilter().getEndDate());
        Integer totalTicketsSold = eventSessionRepository.getTotalTicketsSold(request.getEventSeriesId(),
                request.getEventSessionId(),
                request.getDateRangeFilter().getStartDate(),
                request.getDateRangeFilter().getEndDate());
        Integer totalCapacity = eventSessionRepository.getTotalTicketCapacity(request.getEventSeriesId(),
                request.getEventSessionId(),
                request.getDateRangeFilter().getStartDate(),
                request.getDateRangeFilter().getEndDate());
        double revenuePercentage = 0.0;
        if (maxPotentialRevenue != null && maxPotentialRevenue > 0) {
            double rawPercentage = (voucherRevenue / maxPotentialRevenue) * 100.0;
            revenuePercentage = Math.round(rawPercentage * 100.0) / 100.0;
        }
        double ticketsSoldPercentage = 0.0;
        if (totalCapacity != null && totalCapacity > 0) {
            double rawPercentage = ((double) totalTicketsSold / totalCapacity) * 100.0;
            ticketsSoldPercentage = Math.round(rawPercentage * 100.0) / 100.0;
        }
        return EventOverviewStatsResponse.builder()
                .totalRevenue(totalRevenue)
                .totalResaleRevenue(totalRevenueResale)
                .maxPotentialRevenue(maxPotentialRevenue)
                .voucherRevenue(voucherRevenue)
                .revenuePercentage(revenuePercentage)
                .discountAmount(discountAmount)
                .totalFee(totalFee)
                .totalFeeFromResale(totalFeeResale)
                .totalTicketsSold(totalTicketsSold)
                .totalCapacity(totalCapacity)
                .ticketsSoldPercentage(ticketsSoldPercentage)
                .build();
    }

    @Override
    public List<TopEventRevenueResponse> getTopEventRevenue(
            StatsFilterRequest request) {
        return attendeeRepository.getTopEventRevenue(request);
    }

    @Override
    public List<RevenueChartResponse> getVoucherRevenueWithTimeLabel(StatsFilterRequest request) {
        return bookingRepository.getVoucherRevenueWithTimeLabel(request, BookingType.BUY);
    }


    @Override
    public OrganizerReviewSummaryResponse getOrganizerReviewSummary(StatsFilterRequest request) {
        List<Object[]> reviewRes = reviewRepository.countReviewsByRating(request.getEventSeriesId(),
                request.getEventSessionId(), request.getOrganizerId(),
                request.getDateRangeFilter().getStartDate(), request.getDateRangeFilter().getEndDate());

        int sumCount = 0;
        int sumRating = 0;
        for (Object[] obj : reviewRes) {
            sumCount += ((Number) obj[1]).intValue();
            sumRating += (((Number) obj[0]).intValue() * ((Number) obj[1]).intValue());
        }
        List<CountRatingResponse> ratings = reviewRes.stream().map(
                objects -> CountRatingResponse.builder().rating(((Number) objects[0]).intValue())
                        .count(((Number) objects[1]).intValue()).build()
        ).toList();
        return OrganizerReviewSummaryResponse.builder()
                .totalReview(sumCount)
                .averageRating(sumCount > 0 ? (double) sumRating / sumCount : 0)
                .ratings(ratings)
                .build();
    }
}

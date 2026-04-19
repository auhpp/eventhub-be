package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.AttendeeType;
import com.auhpp.event_management.constant.BookingType;
import com.auhpp.event_management.constant.EventStatus;
import com.auhpp.event_management.constant.SourceType;
import com.auhpp.event_management.dto.request.DateRangeFilterRequest;
import com.auhpp.event_management.dto.response.OrganizerKpiReportResponse;
import com.auhpp.event_management.repository.*;
import com.auhpp.event_management.service.OrganizerStatsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class OrganizerStatsServiceImpl implements OrganizerStatsService {

    BookingRepository bookingRepository;
    EventRepository eventRepository;
    TicketRepository ticketRepository;
    AttendeeRepository attendeeRepository;
    ReviewRepository reviewRepository;

    @Override
    public OrganizerKpiReportResponse getOrganizerKpiReport(Long organizerId, DateRangeFilterRequest request) {

        // revenue
        double totalRevenue = bookingRepository.getTotalRevenue(null, organizerId, null,
                BookingType.BUY, request.getStartDate(), request.getEndDate());

        double totalRevenueAfterVoucher = bookingRepository.getVoucherRevenue(
                organizerId, null, null,
                BookingType.BUY, request.getStartDate(), request.getEndDate());

        double totalFee = attendeeRepository.getCommissionFromEvents(
                organizerId, null, null,
                List.of(SourceType.PURCHASE), request.getStartDate(), request.getEndDate());
        // count event
        int activeEvents = eventRepository.countActiveEvent(organizerId, List.of(EventStatus.APPROVED),
                request.getStartDate(), request.getEndDate());

        int upcomingEvents = eventRepository.countUpcoming(
                organizerId, List.of(EventStatus.PENDING, EventStatus.APPROVED),
                request.getStartDate(), request.getEndDate()
        );

        int pastEvents = eventRepository.countPast(
                organizerId, List.of(EventStatus.PENDING, EventStatus.APPROVED),
                request.getStartDate(), request.getEndDate()
        );

        // count check-in attendee
        int totalTicketSold = ticketRepository.countTicketSold(organizerId,
                request.getStartDate(), request.getEndDate());

        int totalTicketAttendees = attendeeRepository.countTicketCheckIn(AttendeeType.BUY, organizerId,
                request.getStartDate(), request.getEndDate());

        int totalGuestAttendees = attendeeRepository.countTicketCheckIn(AttendeeType.INVITE, organizerId,
                request.getStartDate(), request.getEndDate());

        int totalGuestsInvited = ticketRepository.countTicketInvited(organizerId,
                request.getStartDate(), request.getEndDate());
        // review
        Object[] reviewCount = reviewRepository.countAverage(organizerId,
                request.getStartDate(), request.getEndDate());
        Object[] row = (Object[]) reviewCount[0];
        double avgRating = row[0] != null ? ((Number) row[0]).doubleValue() : 0;
        int totalReviews = row[1] != null ? ((Number) row[1]).intValue() : 0;

        return OrganizerKpiReportResponse.builder()
                .totalRevenue(totalRevenue)
                .totalRevenueAfterVoucher(totalRevenueAfterVoucher)
                .totalFee(totalFee)
                .activeEvents(activeEvents)
                .upcomingEvents(upcomingEvents)
                .pastEvents(pastEvents)
                .totalGuestAttendees(totalGuestAttendees)
                .totalGuestsInvited(totalGuestsInvited)
                .totalTicketAttendees(totalTicketAttendees)
                .totalTicketSold(totalTicketSold)
                .totalRating(avgRating)
                .totalReviews(totalReviews)
                .build();
    }


}

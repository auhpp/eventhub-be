package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.*;
import com.auhpp.event_management.dto.request.EventSessionCreateRequest;
import com.auhpp.event_management.dto.request.EventSessionUpdateRequest;
import com.auhpp.event_management.dto.request.TicketCreateRequest;
import com.auhpp.event_management.dto.response.*;
import com.auhpp.event_management.entity.*;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.EventSessionMapper;
import com.auhpp.event_management.repository.AttendeeRepository;
import com.auhpp.event_management.repository.BookingRepository;
import com.auhpp.event_management.repository.EventRepository;
import com.auhpp.event_management.repository.EventSessionRepository;
import com.auhpp.event_management.service.EventSessionService;
import com.auhpp.event_management.service.TicketService;
import com.auhpp.event_management.util.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EventSessionServiceImpl implements EventSessionService {

    EventSessionRepository eventSessionRepository;
    EventSessionMapper eventSessionMapper;
    TicketService ticketService;
    EventRepository eventRepository;
    BookingRepository bookingRepository;
    AttendeeRepository attendeeRepository;

    @Override
    @Transactional
    public EventSessionResponse createEventSession(EventSessionCreateRequest eventSessionCreateRequest,
                                                   Long eventId) {
        EventSession eventSession = eventSessionMapper.toEventSession(eventSessionCreateRequest);
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        eventSession.setEvent(event);
        eventSessionRepository.save(eventSession);

        for (TicketCreateRequest ticketCreateRequest : eventSessionCreateRequest.getTicketCreateRequests()) {
            ticketService.createTicket(ticketCreateRequest, eventSession.getId());
        }
        return eventSessionMapper.toEventSessionResponse(eventSession);
    }

    @Override
    public EventSessionResponse getEventSessionById(Long id) {
        EventSession eventSession = eventSessionRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        return eventSessionMapper.toEventSessionResponse(eventSession);
    }

    @Override
    @Transactional
    public EventSessionResponse updateEventSession(Long id, EventSessionUpdateRequest request) {
        EventSession eventSession = eventSessionRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (eventSession.isExpired()) {
            throw new AppException(ErrorCode.RESOURCE_CAN_NOT_UPDATE);
        }
        eventSessionMapper.updateEventSessionFromRequest(request, eventSession);
        eventSessionRepository.save(eventSession);
        return eventSessionMapper.toEventSessionResponse(eventSession);
    }

    @Override
    public void deleteById(Long id) {
        EventSession eventSession = eventSessionRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        Event event = eventSession.getEvent();
        boolean valid = true;
        for (Ticket ticket : eventSession.getTickets()) {
            if (!ticket.getAttendees().isEmpty()) {
                valid = false;
                break;
            }
        }
        if (valid && event.getEventSessions().size() > 1 && !eventSession.isExpired()) {
            eventSessionRepository.delete(eventSession);
        } else {
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND);
        }
    }

    @Override
    public EventSessionReportCheckInResponse reportCheckIn(Long id) {
        EventSession eventSession = eventSessionRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        List<Ticket> tickets = eventSession.getTickets();
        int totalQuantity = 0;
        int soldQuantity = 0;
        int checkedInQuantity = 0;
        int outsideQuantity = 0;

        int guestTotalQuantity = 0;
        int guestInvitedQuantity = 0;
        int guestCheckedInQuantity = 0;
        int guestOutsideQuantity = 0;

        List<TicketCheckInResponse> ticketCheckIns = new ArrayList<>();
        for (Ticket ticket : tickets) {
            int totalQuantityTmp = ticket.getQuantity();
            int guestTotalQuantityTmp = ticket.getInvitationQuota();
            totalQuantity += totalQuantityTmp;
            guestTotalQuantity += guestTotalQuantityTmp;

            int checkedInQuantityTmp = ticket.getAttendees().stream().filter(
                    attendee -> attendee.getStatus() == AttendeeStatus.CHECKED_IN &&
                            attendee.getType() == AttendeeType.BUY
            ).toList().size();
            int guestCheckedInQuantityTmp = ticket.getAttendees().stream().filter(
                    attendee -> attendee.getStatus() == AttendeeStatus.CHECKED_IN &&
                            attendee.getType() == AttendeeType.INVITE
            ).toList().size();
            checkedInQuantity += checkedInQuantityTmp;
            guestCheckedInQuantity += guestCheckedInQuantityTmp;

            int soldQuantityTmp = ticket.getSoldQuantity() == null ? 0 : ticket.getSoldQuantity();
            int invitedQuantityTmp = ticket.getInvitedQuantity() == null ? 0 : ticket.getInvitedQuantity();
            soldQuantity += soldQuantityTmp;
            guestInvitedQuantity += invitedQuantityTmp;

            int outsideQuantityTmp = ticket.getAttendees().stream().filter(
                    attendee -> attendee.getStatus() == AttendeeStatus.OUTSIDE &&
                            attendee.getType() == AttendeeType.BUY
            ).toList().size();
            int guestOutsideQuantityTmp = ticket.getAttendees().stream().filter(
                    attendee -> attendee.getStatus() == AttendeeStatus.OUTSIDE &&
                            attendee.getType() == AttendeeType.INVITE
            ).toList().size();
            outsideQuantity += outsideQuantityTmp;
            guestOutsideQuantity += guestOutsideQuantityTmp;

            ticketCheckIns.add(TicketCheckInResponse.builder()
                    .name(ticket.getName())
                    .totalQuantity(totalQuantityTmp)
                    .soldQuantity(soldQuantityTmp)
                    .checkInQuantity(checkedInQuantityTmp)
                    .guestTotalQuantity(guestTotalQuantityTmp)
                    .guestInvitedQuantity(invitedQuantityTmp)
                    .guestCheckedInQuantity(guestCheckedInQuantityTmp)
                    .build());
        }
        return EventSessionReportCheckInResponse.builder()
                .totalQuantity(totalQuantity)
                .soldQuantity(soldQuantity)
                .outsideQuantity(outsideQuantity)
                .checkedInQuantity(checkedInQuantity)
                .guestTotalQuantity(guestTotalQuantity)
                .guestInvitedQuantity(guestInvitedQuantity)
                .guestCheckedInQuantity(guestCheckedInQuantity)
                .guestOutsideQuantity(guestOutsideQuantity)
                .ticketCheckIns(ticketCheckIns)
                .build();
    }

    @Override
    @Transactional
    public void cancelEventSession(Long id) {
        EventSession eventSession = eventSessionRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        String email = SecurityUtils.getCurrentUserLogin();
        if (!Objects.equals(email, eventSession.getEvent().getAppUser().getEmail())) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        if (eventSession.getStatus() == EventSessionStatus.PENDING) {
            eventSession.setStatus(EventSessionStatus.CANCELLED);
            eventSessionRepository.save(eventSession);
        } else if (eventSession.getStatus() == EventSessionStatus.APPROVED) {
            if (eventSession.isOnGoing()) {
                throw new AppException(ErrorCode.EVENT_ON_GOING);
            }
            eventSession.setStatus(EventSessionStatus.CANCELLED);
            eventSessionRepository.save(eventSession);

            List<Booking> bookings = bookingRepository.findAllByEventSessionId(eventSession.getId(), BookingStatus.PAID);
            for (Booking booking : bookings) {
                booking.setStatus(BookingStatus.CANCELLED_BY_EVENT);
            }
            bookingRepository.saveAll(bookings);

            List<Attendee> attendees = attendeeRepository.findAllByEventSessionId(eventSession.getId(), AttendeeStatus.VALID);
            for (Attendee attendee : attendees) {
                attendee.setStatus(AttendeeStatus.CANCELLED_BY_EVENT);
            }
            attendeeRepository.saveAll(attendees);
        }
    }

    @Override
    public EventOverviewStatsResponse getEventStats(Long eventSessionId) {
        Double totalRevenue = eventSessionRepository.getTotalRevenue(eventSessionId);
        Double maxPotentialRevenue = eventSessionRepository.getMaxPotentialRevenue(eventSessionId);
        Double voucherRevenue = eventSessionRepository.getVoucherRevenue(eventSessionId);
        Double discountAmount = eventSessionRepository.getDiscountAmount(eventSessionId);
        Double totalFee = eventSessionRepository.getTotalFee(eventSessionId);

        Integer totalTicketsSold = eventSessionRepository.getTotalTicketsSold(eventSessionId);
        Integer totalCapacity = eventSessionRepository.getTotalTicketCapacity(eventSessionId);
        Double revenuePercentage = 0.0;
        if (maxPotentialRevenue != null && maxPotentialRevenue > 0) {
            double rawPercentage = (voucherRevenue / maxPotentialRevenue) * 100.0;
            revenuePercentage = Math.round(rawPercentage * 100.0) / 100.0;
        }
        Double ticketsSoldPercentage = 0.0;
        if (totalCapacity != null && totalCapacity > 0) {
            double rawPercentage = ((double) totalTicketsSold / totalCapacity) * 100.0;
            ticketsSoldPercentage = Math.round(rawPercentage * 100.0) / 100.0;
        }
        return EventOverviewStatsResponse.builder()
                .totalRevenue(totalRevenue)
                .maxPotentialRevenue(maxPotentialRevenue)
                .voucherRevenue(voucherRevenue)
                .revenuePercentage(revenuePercentage)
                .discountAmount(discountAmount)
                .totalFee(totalFee)
                .totalTicketsSold(totalTicketsSold)
                .totalCapacity(totalCapacity)
                .ticketsSoldPercentage(ticketsSoldPercentage)
                .build();
    }

    @Override
    public EventChartStatsResponse getEventChartStats(Long eventSessionId, TimeFilter filter) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate;
        List<ChartDataProjection> rawDataFromDb;
        List<String> allTimeLabels = new ArrayList<>();
        if (filter == TimeFilter.LAST_24_HOURS) {
            startDate = now.minusHours(23).withMinute(0).withSecond(0).withNano(0);
            rawDataFromDb = eventSessionRepository.getChartDataByHour(eventSessionId, startDate);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:00");
            for (int i = 0; i < 24; i++) {
                allTimeLabels.add(startDate.plusHours(i).format(formatter));
            }
        } else {
            startDate = now.minusDays(29).withHour(0).withMinute(0).withSecond(0).withNano(0);
            rawDataFromDb = eventSessionRepository.getChartDataByDay(eventSessionId, startDate);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
            for (int i = 0; i < 30; i++) {
                allTimeLabels.add(startDate.plusDays(i).format(formatter));
            }
        }
        Map<String, ChartDataProjection> dbDataMap = rawDataFromDb.stream()
                .collect(Collectors.toMap(ChartDataProjection::getTimeLabel, data -> data));
        List<EventChartDataPoint> finalDataPoints = new ArrayList<>();

        for (String label : allTimeLabels) {
            if (dbDataMap.containsKey(label)) {
                ChartDataProjection projection = dbDataMap.get(label);
                finalDataPoints.add(new EventChartDataPoint(
                        label,
                        projection.getRevenue(),
                        projection.getTicketsSold()
                ));
            } else {
                // Nếu giờ/ngày đó KHÔNG có người mua vé -> Cho bằng 0
                finalDataPoints.add(new EventChartDataPoint(label, 0.0, 0));
            }
        }

        return EventChartStatsResponse.builder()
                .timeFilter(filter)
                .data(finalDataPoints)
                .build();
    }

}

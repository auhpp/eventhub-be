package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.AttendeeSearchStatus;
import com.auhpp.event_management.constant.AttendeeStatus;
import com.auhpp.event_management.constant.EventType;
import com.auhpp.event_management.dto.request.AttendeeCreateRequest;
import com.auhpp.event_management.dto.request.AttendeeSearchRequest;
import com.auhpp.event_management.dto.request.CheckInRequest;
import com.auhpp.event_management.dto.request.CheckinSearchRequest;
import com.auhpp.event_management.dto.response.AttendeeBasicResponse;
import com.auhpp.event_management.dto.response.AttendeeResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.entity.*;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.AttendeeBasicMapper;
import com.auhpp.event_management.mapper.AttendeeMapper;
import com.auhpp.event_management.repository.AppUserRepository;
import com.auhpp.event_management.repository.AttendeeRepository;
import com.auhpp.event_management.repository.BookingRepository;
import com.auhpp.event_management.repository.TicketRepository;
import com.auhpp.event_management.service.AttendeeService;
import com.auhpp.event_management.util.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AttendeeServiceImpl implements AttendeeService {
    AttendeeRepository attendeeRepository;
    BookingRepository bookingRepository;
    TicketRepository ticketRepository;
    AttendeeMapper attendeeMapper;
    AppUserRepository appUserRepository;
    AttendeeBasicMapper attendeeBasicMapper;

    static String CHAR_LOWER = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    static SecureRandom random = new SecureRandom();

    public String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int rndCharAt = random.nextInt(CHAR_LOWER.length());
            char rndChar = CHAR_LOWER.charAt(rndCharAt);
            sb.append(rndChar);
        }
        return sb.toString();
    }

    @Override
    public String generateTicketCode() {
        String ticketCode = "";
        boolean isUnique = false;
        do {
            ticketCode = generateRandomString(8);
            if (attendeeRepository.findByTicketCode(ticketCode).isEmpty()) {
                isUnique = true;
            }
        } while (!isUnique);
        return ticketCode;
    }

    @Override
    @Transactional
    public AttendeeBasicResponse checkIn(CheckInRequest request) {
        Attendee attendee = attendeeRepository.findByTicketCode(request.getTicketCode()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        EventSession eventSession = attendee.getTicket().getEventSession();
        if (eventSession.isExpired()) {
            throw new AppException(ErrorCode.EXPIRED_EVENT_SESSION);
        }
        String email = SecurityUtils.getCurrentUserLogin();
        Event event = eventSession.getEvent();
        if (!event.isEventStaff(email) || !Objects.equals(event.getId(), request.getEventId())) {
            throw new AppException(ErrorCode.WRONG_EVENT);
        }

        if (attendee.getStatus() == AttendeeStatus.VALID) {
            attendee.setStatus(AttendeeStatus.CHECKED_IN);
            attendee.setCheckInAt(LocalDateTime.now());
            attendeeRepository.save(attendee);
            return attendeeBasicMapper.toAttendeeBasicResponse(attendee);
        } else if (attendee.getStatus() == AttendeeStatus.CHECKED_IN) {
            throw new AppException(ErrorCode.CHECKED_IN_TICKET);
        } else {
            throw new AppException(ErrorCode.INVALID_TICKET);
        }
    }

    @Override
    @Transactional
    public AttendeeResponse createAttendee(AttendeeCreateRequest attendeeCreateRequest) {
        Attendee attendee = attendeeMapper.toAttendee(attendeeCreateRequest);

        Booking booking = bookingRepository.findById(attendeeCreateRequest.getBookingId()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );

        Ticket ticket = ticketRepository.findById(attendeeCreateRequest.getTicketId()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        attendee.setPrice(ticket.getPrice());
        attendee.setBooking(booking);
        attendee.setTicket(ticket);


        Event event = attendee.getTicket().getEventSession().getEvent();
        if (event.getType() == EventType.OFFLINE) {
            attendee.setTicketCode(generateTicketCode());
        }
        attendeeRepository.save(attendee);
        return attendeeMapper.toAttendeeResponse(attendee);
    }

    @Override
    public AttendeeResponse confirmValidAttendee(Long attendeeId) {
        Attendee attendee = attendeeRepository.findById(attendeeId).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        String email = SecurityUtils.getCurrentUserLogin();
        AppUser user = appUserRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        attendee.setOwner(user);
        attendee.setStatus(AttendeeStatus.VALID);
        attendee.setCreatedAt(LocalDateTime.now());
        attendeeRepository.save(attendee);
        return attendeeMapper.toAttendeeResponse(attendee);
    }

    @Override
    public PageResponse<AttendeeResponse> getAttendeesByCurrentUser(AttendeeSearchRequest attendeeSearchRequest,
                                                                    int page, int size) {
        String email = SecurityUtils.getCurrentUserLogin();

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC,
                "createdAt"));
        Page<Attendee> attendees;
        if (attendeeSearchRequest.getStatus() == AttendeeSearchStatus.COMING) {
            attendees = attendeeRepository.findComingAllByEmailUser(LocalDateTime.now(), email, pageable);
        } else if (attendeeSearchRequest.getStatus() == AttendeeSearchStatus.PAST) {
            attendees = attendeeRepository.findPastAllByEmailUser(LocalDateTime.now(), email, pageable);
        } else if (attendeeSearchRequest.getStatus() == AttendeeSearchStatus.CANCELLED) {
            attendees = attendeeRepository.findAllByStatusAndEmailUser(AttendeeStatus.CANCELLED, email, pageable);
        } else {
            attendees = attendeeRepository.findAllByStatusAndEmailUser(AttendeeStatus.VALID, email, pageable);
        }
        List<AttendeeResponse> attendeeResponses = attendees.getContent().stream().map(
                attendeeMapper::toAttendeeResponse
        ).toList();
        return PageResponse.<AttendeeResponse>builder()
                .currentPage(page)
                .totalElements(attendees.getTotalElements())
                .totalPage(attendees.getTotalPages())
                .pageSize(attendees.getSize())
                .data(attendeeResponses)
                .build();
    }

    @Override
    public AttendeeResponse getAttendeeById(Long id) {
        Attendee attendee = attendeeRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        return attendeeMapper.toAttendeeResponse(attendee);
    }

    @Override
    public AttendeeResponse assignAttendeeEmail(Long id, String email) {
        Attendee attendee = attendeeRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        String emailAuth = SecurityUtils.getCurrentUserLogin();
        if (!Objects.equals(attendee.getBooking().getAppUser().getEmail(), emailAuth)) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        AppUser user = appUserRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        attendee.setOwner(user);
        attendeeRepository.save(attendee);
        return attendeeMapper.toAttendeeResponse(attendee);
    }

    @Override
    public String getMeetingLink(Long attendeeId) {
        Attendee attendee = attendeeRepository.findById(attendeeId).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (attendee.getStatus() == AttendeeStatus.VALID) {
            String email = SecurityUtils.getCurrentUserLogin();
            if (!Objects.equals(email, attendee.getOwner().getEmail())) {
                throw new AppException(ErrorCode.FORBIDDEN);
            }
            EventSession eventSession = attendee.getTicket().getEventSession();
            if (eventSession.getCheckinStartTime() != null && eventSession.getCheckinStartTime().isBefore(LocalDateTime.now())) {
                throw new AppException(ErrorCode.INVALID_TIME_JOIN);
            }
            return attendee.getTicket().getEventSession().getMeetingUrl();
        }
        return "";
    }

    @Override
    public PageResponse<AttendeeResponse> getAttendees(CheckinSearchRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC,
                "createdAt"));
        Page<Attendee> attendeePage;
        if (request.getStatus() != null && request.getEventSessionId() != null) {
            attendeePage = attendeeRepository.findAllByEventSessionIdAndStatus(request.getEventSessionId(),
                    request.getStatus(), pageable);
        } else if (request.getEventSessionId() != null) {
            attendeePage = attendeeRepository.findAllByEventSessionId(request.getEventSessionId(), pageable);
        } else {
            attendeePage = attendeeRepository.findAll(pageable);
        }
        List<AttendeeResponse> attendeeResponses = attendeePage.getContent().stream().map(
                attendeeMapper::toAttendeeResponse
        ).toList();
        return PageResponse.<AttendeeResponse>builder()
                .currentPage(page)
                .totalElements(attendeePage.getTotalElements())
                .totalPage(attendeePage.getTotalPages())
                .pageSize(attendeePage.getSize())
                .data(attendeeResponses)
                .build();
    }


}

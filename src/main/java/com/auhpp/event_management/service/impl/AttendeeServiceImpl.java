package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.AttendeeSearchStatus;
import com.auhpp.event_management.constant.AttendeeStatus;
import com.auhpp.event_management.constant.EventType;
import com.auhpp.event_management.dto.request.AttendeeCreateRequest;
import com.auhpp.event_management.dto.request.AttendeeSearchRequest;
import com.auhpp.event_management.dto.response.AttendeeResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.entity.Attendee;
import com.auhpp.event_management.entity.Booking;
import com.auhpp.event_management.entity.EventSession;
import com.auhpp.event_management.entity.Ticket;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.AttendeeMapper;
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

        if (attendee.getTicket().getEventSession().getEvent().getType() == EventType.OFFLINE) {
            String ticketCode = "";
            boolean isUnique = false;
            do {
                ticketCode = generateRandomString(8);
                if (attendeeRepository.findByTicketCode(ticketCode).isEmpty()) {
                    isUnique = true;
                }
            } while (!isUnique);

            attendee.setTicketCode(ticketCode);
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
        if (attendee.getTicket().getEventSession().getEvent().getType() == EventType.ONLINE) {
            if (attendeeRepository.findByOwnerEmail(email).isEmpty()) {
                attendee.setOwnerEmail(email);
            }
        }
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
        attendee.setOwnerEmail(email);
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
            if (!Objects.equals(email, attendee.getOwnerEmail())) {
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


}

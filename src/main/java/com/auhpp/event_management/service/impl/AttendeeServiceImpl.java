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
import com.auhpp.event_management.dto.response.UserAttendeeSummaryResponse;
import com.auhpp.event_management.entity.*;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.AttendeeBasicMapper;
import com.auhpp.event_management.mapper.AttendeeMapper;
import com.auhpp.event_management.mapper.UserBasicMapper;
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
import java.util.*;
import java.util.stream.Collectors;

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
    UserBasicMapper userBasicMapper;

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
    public AttendeeBasicResponse cancelAttendee(Long id) {
        Attendee attendee = attendeeRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        String email = SecurityUtils.getCurrentUserLogin();
        if (!Objects.equals(email, attendee.getOwner().getEmail())) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        Ticket ticket = attendee.getTicket();
        LocalDateTime deadline = ticket.getEventSession().getStartDateTime().minusMinutes(
                ticket.getCancelBeforeMinutes()
        );
        if (LocalDateTime.now().isAfter(deadline)) {
            throw new AppException(ErrorCode.CANNOT_CANCEL_TICKET);
        }
        ticket.setSoldQuantity(ticket.getSoldQuantity() - 1);
        ticketRepository.save(ticket);

        attendee.setStatus(AttendeeStatus.CANCELLED_BY_USER);
        attendeeRepository.save(attendee);
        return attendeeBasicMapper.toAttendeeBasicResponse(attendee);
    }

    @Override
    @Transactional
    public AttendeeResponse createAttendee(AttendeeCreateRequest request) {
        Attendee attendee = attendeeMapper.toAttendee(request);

        Booking booking = bookingRepository.findById(request.getBookingId()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );

        Ticket ticket = ticketRepository.findById(request.getTicketId()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        attendee.setPrice(ticket.getPrice());
        attendee.setBooking(booking);
        attendee.setTicket(ticket);
        attendee.setDiscountAmount(0D);
        attendee.setFinalPrice(ticket.getPrice());
        if (request.getAttendeeParentId() != null) {
            Attendee attendeeParent = attendeeRepository.findById(request.getAttendeeParentId())
                    .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
            attendee.setParentAttendee(attendeeParent);
        }
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
        if (attendeeSearchRequest.getSearchStatus() == AttendeeSearchStatus.COMING) {
            attendees = attendeeRepository.findComingAllByEmailUser(LocalDateTime.now(), email, pageable);
        } else if (attendeeSearchRequest.getSearchStatus() == AttendeeSearchStatus.PAST) {
            attendees = attendeeRepository.findPastAllByEmailUser(LocalDateTime.now(), email, pageable);
        } else if (attendeeSearchRequest.getSearchStatus() == AttendeeSearchStatus.CANCELLED) {
            attendees = attendeeRepository.findAllByStatusAndEmailUser(
                    List.of(AttendeeStatus.CANCELLED_BY_USER, AttendeeStatus.CANCELLED_BY_EVENT), email, pageable);
        } else if (attendeeSearchRequest.getSearchStatus() == AttendeeSearchStatus.CHECKED_IN) {
            attendees = attendeeRepository.findAllByStatusAndEmailUser(
                    List.of(AttendeeStatus.CHECKED_IN), email, pageable);
        } else {
            attendees = attendeeRepository.findAllByStatusAndEmailUser(
                    List.of(AttendeeStatus.VALID), email, pageable);
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

    @Override
    public PageResponse<UserAttendeeSummaryResponse>
    getUserAttendeeSummaries(Long eventSessionId,
                             AttendeeSearchRequest searchRequest, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.ASC, "email");
        Page<AppUser> appUserPage = attendeeRepository.findUserByEventSession(
                searchRequest.getStatus(),
                eventSessionId,
                pageable
        );
        List<UserAttendeeSummaryResponse> responses = new ArrayList<>();
        if (!appUserPage.isEmpty()) {
            List<AppUser> usersOnPage = appUserPage.getContent();

            List<Attendee> attendees = attendeeRepository.findAllByUserInAndEventSession(null, usersOnPage,
                    eventSessionId);
            Map<Long, List<Attendee>> attendeesByUserId = attendees.stream().collect(
                    Collectors.groupingBy(a -> a.getOwner().getId())
            );
            responses = usersOnPage.stream().map(
                    appUser -> {
                        List<Attendee> userAttendees = attendeesByUserId.getOrDefault(appUser.getId(),
                                Collections.emptyList());
                        return UserAttendeeSummaryResponse.builder()
                                .user(userBasicMapper.toUserBasicResponse(appUser))
                                .attendees(userAttendees.stream().map(attendeeBasicMapper::toAttendeeBasicResponse)
                                        .toList())
                                .build();
                    }
            ).toList();
        }
        return PageResponse.<UserAttendeeSummaryResponse>builder()
                .currentPage(page)
                .totalElements(appUserPage.getTotalElements())
                .totalPage(appUserPage.getTotalPages())
                .pageSize(appUserPage.getSize())
                .data(responses)
                .build();
    }

    @Override
    public String getTicketCode(Long id) {
        Attendee attendee = attendeeRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        String email = SecurityUtils.getCurrentUserLogin();
        if (!Objects.equals(attendee.getOwner().getEmail(), email)) {
            throw new AppException(ErrorCode.ATTENDEE_OWNER_INVALID);
        }
        return attendee.getTicketCode();
    }

    @Override
    public boolean getAttendeeByEventSessionIdAndCurrentUser(Long eventSessionId, Long userId) {
        return attendeeRepository.existsByAppUserIdAndEventSessionId(userId, eventSessionId);
    }
}

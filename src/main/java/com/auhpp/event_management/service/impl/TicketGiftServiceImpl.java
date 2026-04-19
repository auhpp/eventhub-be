package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.*;
import com.auhpp.event_management.dto.request.*;
import com.auhpp.event_management.dto.response.AttendeeBasicResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.TicketGiftResponse;
import com.auhpp.event_management.entity.*;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.AttendeeBasicMapper;
import com.auhpp.event_management.mapper.TicketGiftMapper;
import com.auhpp.event_management.repository.AppUserRepository;
import com.auhpp.event_management.repository.AttendeeRepository;
import com.auhpp.event_management.repository.BookingRepository;
import com.auhpp.event_management.repository.TicketGiftRepository;
import com.auhpp.event_management.service.AttendeeService;
import com.auhpp.event_management.service.EmailService;
import com.auhpp.event_management.service.NotificationService;
import com.auhpp.event_management.service.TicketGiftService;
import com.auhpp.event_management.util.SecurityUtils;
import com.auhpp.event_management.util.SpecBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TicketGiftServiceImpl implements TicketGiftService {

    TicketGiftRepository ticketGiftRepository;
    BookingRepository bookingRepository;
    AppUserRepository appUserRepository;
    AttendeeRepository attendeeRepository;
    TicketGiftMapper ticketGiftMapper;
    EmailService emailService;
    AttendeeService attendeeService;
    AttendeeBasicMapper attendeeBasicMapper;
    NotificationService notificationService;

    @Override
    @Transactional
    public TicketGiftResponse createTicketGift(TicketGiftCreateRequest request) {
        String email = SecurityUtils.getCurrentUserLogin();
        // get info user and booking
        AppUser sender = appUserRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        AppUser receiver = appUserRepository.findById(request.getReceiverId()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        Booking booking = bookingRepository.findById(request.getBookingId()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        TicketGift ticketGift = TicketGift.builder()
                .sender(sender)
                .receiver(receiver)
                .booking(booking)
                .build();

        // handle ticket
        Event event = null;

        List<AttendeeTicketGift> attendeeTicketGifts = new ArrayList<>();
        for (Long attendeeId : request.getAttendeeIds()) {
            Attendee attendee = attendeeRepository.findById(attendeeId).orElseThrow(
                    () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
            );
            EventSession eventSession = attendee.getTicket().getEventSession();
            if (eventSession.isExpired()) {
                throw new AppException(ErrorCode.EXPIRED_EVENT_SESSION);
            }

            if (event == null) {
                event = attendee.getTicket().getEventSession().getEvent();
            }
            if (attendee.getStatus() == AttendeeStatus.VALID) {
                attendeeTicketGifts.add(AttendeeTicketGift.builder()
                        .ticketGift(ticketGift)
                        .attendee(attendee)
                        .build());
                attendee.setType(AttendeeType.GIFT);
                attendee.setStatus(AttendeeStatus.PENDING_GIFT);
                attendeeRepository.save(attendee);
            } else {
                throw new AppException(ErrorCode.INVALID_TICKET);
            }
        }
        ticketGift.setAttendeeTicketGifts(attendeeTicketGifts);
        ticketGift.setStatus(TicketGiftStatus.PENDING);
        ticketGift.setExpiredAt(LocalDateTime.now().plusMinutes(30));
        // save
        ticketGiftRepository.save(ticketGift);

        // send email
        assert event != null;
        emailService.sendTicketGiftEmail(TicketGiftEmailRequest.builder()
                .ticketQuantity(request.getAttendeeIds().size())
                .ticketGiftId(ticketGift.getId())
                .emailReceiver(receiver.getEmail())
                .eventId(event.getId())
                .eventName(event.getName())
                .sender(sender)
                .build());

        // notification
        notificationService.createNotification(
                NotificationRequest.builder()
                        .message("")
                        .recipientIds(List.of(receiver.getId()))
                        .type(NotificationType.GIFT_TICKET)
                        .subjectAvatar(sender.getAvatar())
                        .subjectType(NotificationSubjectType.USER)
                        .subjectId(sender.getId())
                        .subject(!Objects.equals(sender.getFullName(), null) ? sender.getFullName() : sender.getEmail())
                        .targetAvatar(event.getPoster())
                        .targetId(event.getId())
                        .targetType(NotificationTargetType.EVENT)
                        .target(event.getName())
                        .build()
        );

        return ticketGiftMapper.toTicketGiftResponse(ticketGift);
    }

    @Override
    @Transactional
    public TicketGiftResponse accept(Long id) {
        TicketGift ticketGift = ticketGiftRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (LocalDateTime.now().isAfter(ticketGift.getExpiredAt())) {
            throw new AppException(ErrorCode.INVALID_TIME_INVITATION);
        }
        AppUser receiver = ticketGift.getReceiver();
        if (ticketGift.getStatus() == TicketGiftStatus.PENDING) {
            ticketGift.setStatus(TicketGiftStatus.ACCEPTED);

            // Handle attendee
            List<Attendee> attendees = ticketGift.getAttendeeTicketGifts().stream().map(
                    attendeeTicketGift -> {
                        Attendee attendee = attendeeTicketGift.getAttendee();
                        EventSession eventSession = attendee.getTicket().getEventSession();
                        if (eventSession.isExpired()) {
                            throw new AppException(ErrorCode.EXPIRED_EVENT_SESSION);
                        }
                        attendee.setOwner(receiver);
                        attendee.setTicketCode(attendeeService.generateTicketCode());
                        attendee.setType(AttendeeType.GIFT);
                        attendee.setStatus(AttendeeStatus.VALID);
                        return attendee;
                    }
            ).toList();
            attendeeRepository.saveAll(attendees);

            ticketGiftRepository.save(ticketGift);
            return ticketGiftMapper.toTicketGiftResponse(ticketGift);
        } else {
            throw new AppException(ErrorCode.RESOURCE_CAN_NOT_UPDATE);
        }
    }

    private void resetAttendeeType(TicketGift ticketGift, boolean checkExpired) {
        List<Attendee> attendees = ticketGift.getAttendeeTicketGifts().stream().map(
                attendeeTicketGift -> {
                    Attendee attendee = attendeeTicketGift.getAttendee();
                    if (checkExpired) {
                        EventSession eventSession = attendee.getTicket().getEventSession();
                        if (eventSession.isExpired()) {
                            throw new AppException(ErrorCode.EXPIRED_EVENT_SESSION);
                        }
                    }
                    attendee.setType(attendee.getSourceType() == SourceType.PURCHASE ? AttendeeType.BUY : AttendeeType.RESALE);
                    return attendee;
                }
        ).toList();
        attendeeRepository.saveAll(attendees);
    }

    private void resetAttendeeStatus(TicketGift ticketGift) {
        List<Attendee> attendees = ticketGift.getAttendeeTicketGifts().stream().map(
                attendeeTicketGift -> {
                    Attendee attendee = attendeeTicketGift.getAttendee();
                    attendee.setStatus(AttendeeStatus.VALID);
                    return attendee;
                }
        ).toList();
        attendeeRepository.saveAll(attendees);
    }

    @Override
    @Transactional
    public TicketGiftResponse reject(Long id, EventInvitationRejectRequest eventInvitationRejectRequest) {
        TicketGift ticketGift = ticketGiftRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (LocalDateTime.now().isAfter(ticketGift.getExpiredAt())) {
            throw new AppException(ErrorCode.INVALID_TIME_INVITATION);
        }
        if (ticketGift.getStatus() == TicketGiftStatus.PENDING) {
            ticketGift.setStatus(TicketGiftStatus.REJECTED);
            resetAttendeeType(ticketGift, true);
            ticketGift.setRejectionMessage(eventInvitationRejectRequest.getRejectionMessage());

            resetAttendeeStatus(ticketGift);

            ticketGiftRepository.save(ticketGift);
            return ticketGiftMapper.toTicketGiftResponse(ticketGift);
        } else {
            throw new AppException(ErrorCode.RESOURCE_CAN_NOT_UPDATE);
        }
    }

    @Override
    @Transactional
    public TicketGiftResponse revoke(Long id) {
        TicketGift ticketGift = ticketGiftRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (LocalDateTime.now().isAfter(ticketGift.getExpiredAt())) {
            throw new AppException(ErrorCode.INVALID_TIME_INVITATION);
        }
        if (ticketGift.getStatus() == TicketGiftStatus.PENDING) {
            ticketGift.setStatus(TicketGiftStatus.REVOKED);
            resetAttendeeType(ticketGift, false);

            resetAttendeeStatus(ticketGift);

            ticketGiftRepository.save(ticketGift);
            return ticketGiftMapper.toTicketGiftResponse(ticketGift);
        } else {
            throw new AppException(ErrorCode.RESOURCE_CAN_NOT_UPDATE);
        }
    }

    @Override
    public PageResponse<TicketGiftResponse> getTicketGifts(TicketGiftSearchRequest request, int page, int size) {
        Specification<TicketGift> spec = Specification.allOf();
        if (request.getSenderId() != null) {
            spec = spec.and(SpecBuilder.create("id", "=", request.getSenderId(), "sender"));
        } else if (request.getReceiverId() != null) {
            spec = spec.and(SpecBuilder.create("id", "=", request.getReceiverId(), "receiver"));
        }
        if (request.getReceiverEmail() != null) {
            spec = spec.and(SpecBuilder.create("email", "=", request.getReceiverEmail(), "receiver"));
        } else if (request.getSenderEmail() != null) {
            spec = spec.and(SpecBuilder.create("email", "=", request.getSenderEmail(), "sender"));
        }
        if (request.getStatus() != null) {
            spec = spec.and(SpecBuilder.create("status", "=", request.getStatus()));
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC,
                "createdAt"));
        Page<TicketGift> ticketGiftPage = ticketGiftRepository.findAll(spec, pageable);
        List<TicketGiftResponse> responses = ticketGiftPage.getContent().stream().map(
                ticketGiftMapper::toTicketGiftResponse
        ).toList();
        return PageResponse.<TicketGiftResponse>builder()
                .currentPage(page)
                .totalElements(ticketGiftPage.getTotalElements())
                .totalPage(ticketGiftPage.getTotalPages())
                .pageSize(ticketGiftPage.getSize())
                .data(responses)
                .build();
    }

    @Override
    public TicketGiftResponse getById(Long id) {
        TicketGift ticketGift = ticketGiftRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        return ticketGiftMapper.toTicketGiftResponse(ticketGift);
    }

    @Override
    public List<AttendeeBasicResponse> getAttendees(Long id) {
        TicketGift ticketGift = ticketGiftRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        return ticketGift.getAttendeeTicketGifts().stream().map(
                attendeeTicketGift -> attendeeBasicMapper
                        .toAttendeeBasicResponse(attendeeTicketGift.getAttendee())
        ).toList();
    }

    @Override
    @Scheduled(fixedRate = 60000, initialDelay = 5000) // 60000 ms = 1 minute, run after 5s server first init
    @Transactional
    public void cleanupExpiredTicketGifts() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<TicketGift> expiredTicketGifts = ticketGiftRepository.findAllByStatusAndExpiredAtBefore(
                TicketGiftStatus.PENDING, currentDateTime
        );
        if (expiredTicketGifts.isEmpty()) {
            return;
        }
        for (TicketGift ticketGift : expiredTicketGifts) {
            ticketGift.setStatus(TicketGiftStatus.EXPIRED);
            resetAttendeeType(ticketGift, false);

            resetAttendeeStatus(ticketGift);

            ticketGiftRepository.save(ticketGift);
        }
    }
}

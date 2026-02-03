package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.InvitationStatus;
import com.auhpp.event_management.constant.RoleName;
import com.auhpp.event_management.dto.request.EventInvitationCreateRequest;
import com.auhpp.event_management.dto.request.EventInvitationRejectRequest;
import com.auhpp.event_management.dto.request.EventInvitationSearchRequest;
import com.auhpp.event_management.dto.request.InvitationBookingCreateRequest;
import com.auhpp.event_management.dto.response.EventInvitationResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.entity.*;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.EventInvitationMapper;
import com.auhpp.event_management.repository.AppUserRepository;
import com.auhpp.event_management.repository.EventInvitationRepository;
import com.auhpp.event_management.repository.RoleRepository;
import com.auhpp.event_management.repository.TicketRepository;
import com.auhpp.event_management.service.BookingService;
import com.auhpp.event_management.service.EmailService;
import com.auhpp.event_management.service.EventInvitationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EventInvitationServiceImpl implements EventInvitationService {

    EventInvitationRepository eventInvitationRepository;
    TicketRepository ticketRepository;
    EventInvitationMapper eventInvitationMapper;
    EmailService emailService;
    AppUserRepository appUserRepository;
    PasswordEncoder passwordEncoder;
    BookingService bookingService;
    RoleRepository roleRepository;

    @Override
    @Transactional
    public List<EventInvitationResponse> createEventInvitation(EventInvitationCreateRequest eventInvitationCreateRequest) {
        List<EventInvitationResponse> invitationResponses = new ArrayList<>();
        List<EventInvitation> eventInvitations = new ArrayList<>();
        Ticket ticket = ticketRepository.findById(eventInvitationCreateRequest.getTicketId()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        EventSession eventSession = ticket.getEventSession();

        // Check valid time
        if (LocalDateTime.now().isAfter(eventSession.getStartDateTime())) {
            throw new AppException(ErrorCode.INVALID_TIME_INVITATION);
        }
        for (String email : eventInvitationCreateRequest.getEmails()) {
            // Check valid email
//            Optional<Attendee> attendeeOptional = attendeeRepository.findByTicketIdAndEmailUserAndStatus(
//                    eventInvitationCreateRequest.getTicketId(), email, AttendeeStatus.VALID
//            );
            EventInvitationResponse response;
//            if (attendeeOptional.isEmpty()) {
            String token = UUID.randomUUID().toString();
            LocalDateTime next2Days = LocalDateTime.now().plusDays(2);
            LocalDateTime eventSessionStartDateTime = eventSession.getStartDateTime();
            LocalDateTime expiredAt;

            LocalDateTime expiredAtRequest = eventInvitationCreateRequest.getExpiredAt();
            if (expiredAtRequest != null &&
                    expiredAtRequest.isAfter(eventSession.getStartDateTime())
                    && expiredAtRequest.isBefore(LocalDateTime.now())) {
                expiredAt = eventInvitationCreateRequest.getExpiredAt();
            } else {
                expiredAt = next2Days.isAfter(eventSessionStartDateTime) ?
                        eventSessionStartDateTime : next2Days;
            }

            EventInvitation eventInvitation = EventInvitation.builder()
                    .token(token)
                    .status(InvitationStatus.PENDING)
                    .message(eventInvitationCreateRequest.getMessage())
                    .initialQuantity(eventInvitationCreateRequest.getInitialQuantity())
                    .email(email)
                    .expiredAt(expiredAt)
                    .ticket(ticket)
                    .build();
            eventInvitations.add(eventInvitation);
            response = eventInvitationMapper.toEventInvitationResponse(
                    eventInvitation
            );
            response.setSendSuccess(true);
//            }
//        else {
//                response = EventInvitationResponse.builder()
//                        .email(email)
//                        .isSendSuccess(false)
//                        .build();
//            }
            invitationResponses.add(response);
        }

        // Check quantity ticket, date open and end event
        int totalQuantity = eventInvitationCreateRequest.getInitialQuantity() * eventInvitations.size();
        int invitedQuan = ticket.getInvitedQuantity() == null ? 0 : ticket.getInvitedQuantity();
        if (totalQuantity > (ticket.getInvitationQuota() - invitedQuan)) {
            throw new AppException(ErrorCode.NOT_ENOUGH_QUANTITY);
        }
        ticket.setInvitedQuantity(invitedQuan + totalQuantity);
        ticketRepository.save(ticket);
        // send email
        Event event = eventSession.getEvent();
        for (EventInvitationResponse eResponse : invitationResponses) {
            if (eResponse.isSendSuccess()) {
                emailService.sendEventInvitationEmail(eResponse.getEmail(), eResponse.getToken(), event, eventSession,
                        eResponse.getMessage());
            }
        }
        eventInvitationRepository.saveAll(eventInvitations);
        return invitationResponses;
    }

    @Override
    @Transactional
    public EventInvitationResponse acceptEventInvitation(String token) {
        EventInvitation eventInvitation = eventInvitationRepository.findByToken(token).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        //Check expired time
        if (eventInvitation.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.INVALID_TIME_INVITATION);
        }
        if (eventInvitation.getStatus() != InvitationStatus.PENDING) {
            throw new AppException(ErrorCode.RESOURCE_CAN_NOT_UPDATE);
        }
        eventInvitation.setStatus(InvitationStatus.ACCEPTED);

        //Check user
        Optional<AppUser> appUserOptional = appUserRepository.findByEmail(eventInvitation.getEmail());
        AppUser guest = appUserOptional.orElseGet(() -> AppUser.builder()
                .email(eventInvitation.getEmail())
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .status(false)
                .role(roleRepository.findByName(RoleName.USER))
                .build());
        if (appUserOptional.isEmpty()) {
            appUserRepository.save(guest);
        }
        eventInvitation.setAppUser(guest);

        // create attendee and booking
        Booking booking = bookingService.createInvitationBooking(InvitationBookingCreateRequest
                .builder()
                .user(guest)
                .quantity(eventInvitation.getInitialQuantity())
                .ticketId(eventInvitation.getTicket().getId())
                .build());
        eventInvitation.setBooking(booking);
        eventInvitationRepository.save(eventInvitation);
        return eventInvitationMapper.toEventInvitationResponse(eventInvitation);
    }

    @Override
    @Transactional
    public EventInvitationResponse rejectEventInvitation(String token, EventInvitationRejectRequest request) {
        EventInvitation eventInvitation = eventInvitationRepository.findByToken(token).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        //Check expired time
        if (eventInvitation.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.INVALID_TIME_INVITATION);
        }
        if (eventInvitation.getStatus() != InvitationStatus.PENDING) {
            throw new AppException(ErrorCode.RESOURCE_CAN_NOT_UPDATE);
        }
        eventInvitation.setStatus(InvitationStatus.REJECTED);

        //Check user
        Optional<AppUser> appUserOptional = appUserRepository.findByEmail(eventInvitation.getEmail());
        appUserOptional.ifPresent(eventInvitation::setAppUser);
        eventInvitation.setRejectionMessage(request.getRejectionMessage());

        Ticket ticket = eventInvitation.getTicket();
        ticket.setInvitedQuantity(ticket.getInvitedQuantity() - eventInvitation.getInitialQuantity());
        ticketRepository.save(ticket);
        eventInvitationRepository.save(eventInvitation);
        return eventInvitationMapper.toEventInvitationResponse(eventInvitation);
    }

    @Override
    public PageResponse<EventInvitationResponse> getEventInvitations(EventInvitationSearchRequest request,
                                                                     int page, int size) {
        List<InvitationStatus> statuses = new ArrayList<>();
        if (request.getStatus() == null) {
            statuses.addAll(List.of(InvitationStatus.PENDING,
                    InvitationStatus.REJECTED, InvitationStatus.ACCEPTED, InvitationStatus.REVOKED));
        } else {
            statuses.add(request.getStatus());
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC,
                "createdAt"));
        Page<EventInvitation> eventInvitationPage = eventInvitationRepository.findByEventSessionId(
                statuses,
                request.getEventSessionId(),
                pageable);
        List<EventInvitationResponse> responses = eventInvitationPage.stream().map(
                eventInvitationMapper::toEventInvitationResponse
        ).toList();
        return PageResponse.<EventInvitationResponse>builder()
                .currentPage(page)
                .totalElements(eventInvitationPage.getTotalElements())
                .totalPage(eventInvitationPage.getTotalPages())
                .pageSize(eventInvitationPage.getSize())
                .data(responses)
                .build();
    }

    @Override
    public EventInvitationResponse getByToken(String token) {
        EventInvitation eventInvitation = eventInvitationRepository.findByToken(token).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        return eventInvitationMapper.toEventInvitationResponse(eventInvitation);
    }

    @Override
    public void revokeEventInvitation(Long id) {
        EventInvitation eventInvitation = eventInvitationRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (eventInvitation.getStatus() == InvitationStatus.PENDING) {
            eventInvitation.setStatus(InvitationStatus.REVOKED);
            Ticket ticket = eventInvitation.getTicket();
            ticket.setInvitedQuantity(ticket.getInvitedQuantity() - eventInvitation.getInitialQuantity());
            ticketRepository.save(ticket);
            eventInvitationRepository.save(eventInvitation);
        }
    }

    @Override
    @Scheduled(fixedRate = 60000, initialDelay = 5000) // 60000 ms = 1 minute, run after 5s server first init
    @Transactional
    public void cleanupExpiredEventInvitations() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<EventInvitation> expiredEventInvitations = eventInvitationRepository.findAllByStatusAndExpiredAtBefore(
                InvitationStatus.PENDING, currentDateTime
        );
        if (expiredEventInvitations.isEmpty()) {
            return;
        }
        for (EventInvitation eventInvitation : expiredEventInvitations) {
            eventInvitation.setStatus(InvitationStatus.EXPIRED);
            Ticket ticket = eventInvitation.getTicket();
            ticket.setInvitedQuantity(ticket.getInvitedQuantity() - eventInvitation.getInitialQuantity());
            ticketRepository.save(ticket);
            eventInvitationRepository.save(eventInvitation);
        }
    }
}

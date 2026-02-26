package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.EventStaffStatus;
import com.auhpp.event_management.constant.RoleName;
import com.auhpp.event_management.dto.request.EventInvitationRejectRequest;
import com.auhpp.event_management.dto.request.EventStaffCreateRequest;
import com.auhpp.event_management.dto.request.EventStaffSearchRequest;
import com.auhpp.event_management.dto.request.StaffInvitationEmailRequest;
import com.auhpp.event_management.dto.response.EventStaffInvitationResponse;
import com.auhpp.event_management.dto.response.EventStaffResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.entity.AppUser;
import com.auhpp.event_management.entity.Event;
import com.auhpp.event_management.entity.EventStaff;
import com.auhpp.event_management.entity.Role;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.EventStaffMapper;
import com.auhpp.event_management.repository.AppUserRepository;
import com.auhpp.event_management.repository.EventRepository;
import com.auhpp.event_management.repository.EventStaffRepository;
import com.auhpp.event_management.repository.RoleRepository;
import com.auhpp.event_management.service.EmailService;
import com.auhpp.event_management.service.EventStaffService;
import com.auhpp.event_management.util.SecurityUtils;
import com.auhpp.event_management.util.SpecBuilder;
import com.auhpp.event_management.util.ValidateUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EventStaffServiceImpl implements EventStaffService {
    EventStaffRepository eventStaffRepository;
    RoleRepository roleRepository;
    AppUserRepository appUserRepository;
    EventRepository eventRepository;
    EventStaffMapper eventStaffMapper;
    EmailService emailService;

    @Override
    @Transactional
    public List<EventStaffInvitationResponse> createEventStaff(EventStaffCreateRequest request) {
        Role role = roleRepository.findByName(request.getRoleName());
        Event event = eventRepository.findById(request.getEventId()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        List<EventStaffInvitationResponse> responses = new ArrayList<>();
        for (String email : request.getEmails()) {
            EventStaffInvitationResponse response = EventStaffInvitationResponse.builder()
                    .email(email)
                    .build();

            Optional<EventStaff> optionalEventStaff = eventStaffRepository
                    .findByEmailAndEventId(email, request.getEventId());

            EventStaff eventStaff = new EventStaff();

            if (!ValidateUtils.isValidEmail(email)) {
                response.setMessage("Email sai định dạng");
                response.setSendSuccess(false);
                responses.add(response);
                continue;
            }

            if (optionalEventStaff.isPresent()) {
                eventStaff = optionalEventStaff.get();
                if (eventStaff.getStatus() == EventStaffStatus.ACTIVE) {
                    response.setMessage("Email này đã là thành viên");
                    response.setSendSuccess(false);
                    responses.add(response);
                    continue;
                }
            }
            Optional<AppUser> user = appUserRepository.findByEmail(email);
            if (user.isPresent()) {
                eventStaff.setAppUser(user.get());
            }
            eventStaff.setRole(role);
            eventStaff.setMessage(request.getMessage());
            eventStaff.setEvent(event);
            if (request.getExpiredAt() == null) {
                eventStaff.setExpiredAt(LocalDateTime.now().plusHours(1));
            } else {
                if (request.getExpiredAt().isBefore(LocalDateTime.now())) {
                    throw new AppException(ErrorCode.INVALID_TIME_INVITATION);
                }
                eventStaff.setExpiredAt(request.getExpiredAt());
            }

            if (role.getName() != RoleName.EVENT_OWNER) {
                String token = UUID.randomUUID().toString();
                eventStaff.setToken(token);
                eventStaff.setStatus(EventStaffStatus.PENDING);
                eventStaff.setEmail(email);

                // send email
                emailService.sendStaffInvitationEmail(StaffInvitationEmailRequest.builder()
                        .event(event)
                        .message(request.getMessage())
                        .recipientEmail(email)
                        .token(token)
                        .roleName(request.getRoleName())
                        .build());
            } else if (Objects.equals(event.getAppUser().getEmail(), SecurityUtils.getCurrentUserLogin())) {
                AppUser appUser = appUserRepository.findByEmail(email).orElseThrow(
                        () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
                );
                eventStaff.setAppUser(appUser);
                eventStaff.setEmail(appUser.getEmail());
                eventStaff.setStatus(EventStaffStatus.ACTIVE);
            } else {
                throw new AppException(ErrorCode.INVALID_PARAMS);
            }
            response.setSendSuccess(true);
            responses.add(response);
            eventStaffRepository.save(eventStaff);
        }

        return responses;
    }

    @Override
    @Transactional
    public EventStaffResponse acceptInvitation(String token) {
        EventStaff eventStaff = eventStaffRepository.findByToken(token).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (eventStaff.getAppUser() == null) {
            Optional<AppUser> user = appUserRepository.findByEmail(eventStaff.getEmail());
            user.ifPresent(eventStaff::setAppUser);
        }
        String email = SecurityUtils.getCurrentUserLogin();
        if (!Objects.equals(email, eventStaff.getEmail())) {
            throw new AppException(ErrorCode.INVALID_ACCOUNT_LOGIN);
        }
        if (eventStaff.getStatus() == EventStaffStatus.PENDING &&
                eventStaff.getExpiredAt().isAfter(LocalDateTime.now())) {
            eventStaff.setStatus(EventStaffStatus.ACTIVE);
            eventStaffRepository.save(eventStaff);
            return eventStaffMapper.toEventStaffResponse(eventStaff);
        } else {
            throw new AppException(ErrorCode.RESOURCE_CAN_NOT_UPDATE);
        }
    }

    @Override
    @Transactional
    public EventStaffResponse rejectInvitation(String token,
                                               EventInvitationRejectRequest eventInvitationRejectRequest) {
        EventStaff eventStaff = eventStaffRepository.findByToken(token).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (eventStaff.getStatus() == EventStaffStatus.PENDING &&
                eventStaff.getExpiredAt().isAfter(LocalDateTime.now())) {
            eventStaff.setStatus(EventStaffStatus.REJECTED);
            eventStaff.setRejectionMessage(eventInvitationRejectRequest.getRejectionMessage());
            eventStaffRepository.save(eventStaff);
            return eventStaffMapper.toEventStaffResponse(eventStaff);
        } else {
            throw new AppException(ErrorCode.RESOURCE_CAN_NOT_UPDATE);
        }
    }

    @Override
    @Transactional
    public EventStaffResponse revokeInvitation(Long id) {
        EventStaff eventStaff = eventStaffRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        String email = SecurityUtils.getCurrentUserLogin();
        if (!Objects.equals(email, eventStaff.getEvent().getAppUser().getEmail())) {
            throw new AppException(ErrorCode.INVALID_ACCOUNT_LOGIN);
        }
        if (eventStaff.getStatus() == EventStaffStatus.PENDING &&
                eventStaff.getExpiredAt().isAfter(LocalDateTime.now())) {
            eventStaff.setStatus(EventStaffStatus.REVOKED);
            eventStaffRepository.save(eventStaff);
            return eventStaffMapper.toEventStaffResponse(eventStaff);
        } else {
            throw new AppException(ErrorCode.RESOURCE_CAN_NOT_UPDATE);
        }
    }

    @Override
    public PageResponse<EventStaffResponse> getEventStaffs(EventStaffSearchRequest request,
                                                           int page, int size) {
        Specification<EventStaff> spec = Specification.allOf();
        if (request.getEventId() != null) {
            spec = spec.and(SpecBuilder.create("id", "=", request.getEventId(), "event"));
        }
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            spec = spec.and(SpecBuilder.create("email", "=", request.getEmail()));
        }
        if (request.getStatus() != null) {
            spec = spec.and(SpecBuilder.create("status", "=", request.getStatus()));
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC,
                "createdAt"));
        Page<EventStaff> eventStaffPage = eventStaffRepository.findAll(spec, pageable);
        List<EventStaffResponse> responses = eventStaffPage.getContent().stream().map(
                eventStaffMapper::toEventStaffResponse
        ).toList();
        return PageResponse.<EventStaffResponse>builder()
                .currentPage(page)
                .totalElements(eventStaffPage.getTotalElements())
                .totalPage(eventStaffPage.getTotalPages())
                .pageSize(eventStaffPage.getSize())
                .data(responses)
                .build();
    }

    @Override
    public EventStaffResponse getByToken(String token) {
        EventStaff eventStaff = eventStaffRepository.findByToken(token).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        return eventStaffMapper.toEventStaffResponse(eventStaff);
    }

    @Override
    public EventStaffResponse getById(Long id) {
        EventStaff eventStaff = eventStaffRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        return eventStaffMapper.toEventStaffResponse(eventStaff);
    }


}

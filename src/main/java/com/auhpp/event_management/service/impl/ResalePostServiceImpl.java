package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.*;
import com.auhpp.event_management.dto.request.RejectionRequest;
import com.auhpp.event_management.dto.request.ResalePostCreateRequest;
import com.auhpp.event_management.dto.request.ResalePostSearchRequest;
import com.auhpp.event_management.dto.request.ResalePostUpdateRequest;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.ResalePostResponse;
import com.auhpp.event_management.entity.*;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.ResalePostMapper;
import com.auhpp.event_management.repository.*;
import com.auhpp.event_management.service.ResalePostService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ResalePostServiceImpl implements ResalePostService {
    ResalePostMapper resalePostMapper;
    ResalePostRepository resalePostRepository;
    AppUserRepository appUserRepository;
    AttendeeRepository attendeeRepository;
    TicketRepository ticketRepository;
    SystemConfigurationRepository configurationRepository;

    private void checkValidPrice(Double price, Double pricePerTicket) {
        SystemConfiguration minResaleRate = configurationRepository.findByKey(SystemConfigurationKey.MIN_RESALE_RATE);
        SystemConfiguration maxResaleRate = configurationRepository.findByKey(SystemConfigurationKey.MAX_RESALE_RATE);
        double minPrice = (price * minResaleRate.getValue()) / 100.0;
        double maxPrice = (price * maxResaleRate.getValue()) / 100.0;
        if (pricePerTicket < minPrice || pricePerTicket > maxPrice) {
            throw new AppException(ErrorCode.RESALE_PRICE_INVALID);
        }
    }

    @Override
    @Transactional
    public ResalePostResponse create(ResalePostCreateRequest request) {
        String email = SecurityUtils.getCurrentUserLogin();
        AppUser appUser = appUserRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );

        // check valid price
        Ticket ticket = ticketRepository.findById(request.getTicketId()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        checkValidPrice(ticket.getPrice(), request.getPricePerTicket());

        // check event session
        EventSession eventSession = ticket.getEventSession();
        if (eventSession.isOnGoing()) {
            throw new AppException(ErrorCode.EVENT_ON_GOING);
        }
        if (eventSession.getStatus() != EventSessionStatus.APPROVED) {
            throw new AppException(ErrorCode.EVENT_SERIES_NOT_ACTIVE);
        }
        if (eventSession.getEvent().getStatus() != EventStatus.APPROVED) {
            throw new AppException(ErrorCode.EVENT_SERIES_NOT_ACTIVE);
        }

        SystemConfiguration commissionRate = configurationRepository.findByKey(SystemConfigurationKey.RESALE_COMMISSION_RATE);

        ResalePost resalePost = ResalePost.builder()
                .pricePerTicket(request.getPricePerTicket())
                .hasRetail(request.getHasRetail())
                .status(ResalePostStatus.PENDING)
                .commissionRate(commissionRate.getValue())
                .appUser(appUser)
                .build();

        List<Attendee> attendeeResales = new ArrayList<>();
        // check attendee
        for (Long attendeeId : request.getAttendeeIds()) {
            Attendee attendee = attendeeRepository.findById(attendeeId).orElseThrow(
                    () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
            );
            // check status
            if (attendee.getStatus() != AttendeeStatus.VALID) {
                throw new AppException(ErrorCode.ATTENDEE_STATUS_INVALID);
            }
            // check type
            if (attendee.getType() == AttendeeType.INVITE) {
                throw new AppException(ErrorCode.ATTENDEE_TYPE_INVALID);
            }
            // check source type
            if (attendee.getSourceType() == SourceType.GIFT || attendee.getSourceType() == SourceType.INVITATION) {
                throw new AppException(ErrorCode.ATTENDEE_SOURCE_TYPE_INVALID);
            }
            // owner
            if (!Objects.equals(attendee.getOwner().getId(), appUser.getId())) {
                throw new AppException(ErrorCode.ATTENDEE_OWNER_INVALID);
            }
            // update status
            attendee.setStatus(AttendeeStatus.ON_RESALE);
            attendee.setResalePost(resalePost);
            attendeeResales.add(attendee);
        }
        resalePost.setAttendees(attendeeResales);
        resalePostRepository.save(resalePost);
        return resalePostMapper.toResalePostResponse(resalePost);
    }

    @Override
    @Transactional
    public ResalePostResponse update(Long id, ResalePostUpdateRequest request) {
        ResalePost resalePost = resalePostRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (request.getPricePerTicket() != null) {
            // check valid price
            Ticket ticket = ticketRepository.findById(request.getTicketId()).orElseThrow(
                    () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
            );
            checkValidPrice(ticket.getPrice(), resalePost.getPricePerTicket());
            resalePost.setPricePerTicket(request.getPricePerTicket());
        }
        if (request.getHasRetail() != null) {
            resalePost.setHasRetail(request.getHasRetail());
        }
        resalePostRepository.save(resalePost);
        return resalePostMapper.toResalePostResponse(resalePost);
    }

    @Override
    @Transactional
    public void cancelPost(Long id) {
        ResalePost resalePost = resalePostRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        resalePost.setStatus(ResalePostStatus.CANCELLED_BY_USER);
        for (Attendee attendee : resalePost.getAttendees()) {
            if (attendee.getStatus() == AttendeeStatus.ON_RESALE) {
                attendee.setStatus(AttendeeStatus.VALID);
            }
        }
        resalePostRepository.save(resalePost);
    }

    @Override
    @Transactional
    public void rejectPost(Long id, RejectionRequest request) {
        ResalePost resalePost = resalePostRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (resalePost.getStatus() == ResalePostStatus.PENDING) {
            resalePost.setStatus(ResalePostStatus.REJECTED);
            resalePost.setRejectionMessage(request.getReason());
            for (Attendee attendee : resalePost.getAttendees()) {
                if (attendee.getStatus() == AttendeeStatus.ON_RESALE) {
                    attendee.setStatus(AttendeeStatus.VALID);
                }
            }
            resalePostRepository.save(resalePost);
        }
    }

    @Override
    @Transactional
    public void cancelPostByAdmin(Long id, RejectionRequest request) {
        ResalePost resalePost = resalePostRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        resalePost.setStatus(ResalePostStatus.CANCELLED_BY_ADMIN);
        resalePost.setRejectionMessage(request.getReason());
        for (Attendee attendee : resalePost.getAttendees()) {
            if (attendee.getStatus() == AttendeeStatus.ON_RESALE) {
                attendee.setStatus(AttendeeStatus.VALID);
            }
        }
        resalePostRepository.save(resalePost);
    }

    @Override
    public PageResponse<ResalePostResponse> filter(ResalePostSearchRequest request, int page, int size) {
        String sortBy = "";
        Sort.Direction direction = null;
        if (request.getSortType() == SortType.NEWEST || request.getSortType() == null) {
            sortBy = "rp.createdAt";
            direction = Sort.Direction.DESC;
        } else if (request.getSortType() == SortType.OLDEST) {
            sortBy = "rp.createdAt";
            direction = Sort.Direction.ASC;
        } else if (request.getSortType() == SortType.PRICE_HIGHEST) {
            sortBy = "rp.pricePerTicket";
            direction = Sort.Direction.DESC;
        } else if (request.getSortType() == SortType.PRICE_LOWEST) {
            sortBy = "rp.pricePerTicket";
            direction = Sort.Direction.ASC;
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(direction, sortBy));
        Page<ResalePost> pageData = resalePostRepository.filter(
                request.getEventSessionId(), request.getTicketId(),
                request.getHasRetail(), request.getQuantity(), request.getUserId(), request.getStatuses(),
                pageable
        );
        List<ResalePostResponse> responses = pageData.getContent().stream().map(
                resalePostMapper::toResalePostResponse
        ).toList();
        return PageResponse.<ResalePostResponse>builder()
                .currentPage(page)
                .totalElements(pageData.getTotalElements())
                .totalPage(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .data(responses)
                .build();
    }

    @Override
    public ResalePostResponse getById(Long id) {
        ResalePost resalePost = resalePostRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        return resalePostMapper.toResalePostResponse(resalePost);
    }

    @Override
    @Transactional
    public void approvePost(Long id) {
        ResalePost resalePost = resalePostRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (resalePost.getStatus() == ResalePostStatus.PENDING) {
            SystemConfiguration resaleCommissionRate =
                    configurationRepository.findByKey(SystemConfigurationKey.RESALE_COMMISSION_RATE);
            resalePost.setStatus(ResalePostStatus.APPROVED);
            resalePost.setCommissionRate(resaleCommissionRate.getValue());
            resalePostRepository.save(resalePost);
        }
    }
}

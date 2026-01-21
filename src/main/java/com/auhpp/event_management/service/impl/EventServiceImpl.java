package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.EventStatus;
import com.auhpp.event_management.constant.FolderName;
import com.auhpp.event_management.constant.RoleName;
import com.auhpp.event_management.constant.TicketStatus;
import com.auhpp.event_management.dto.request.*;
import com.auhpp.event_management.dto.response.EventResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.entity.AppUser;
import com.auhpp.event_management.entity.Category;
import com.auhpp.event_management.entity.Event;
import com.auhpp.event_management.entity.Ticket;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.EventMapper;
import com.auhpp.event_management.repository.AppUserRepository;
import com.auhpp.event_management.repository.CategoryRepository;
import com.auhpp.event_management.repository.EventRepository;
import com.auhpp.event_management.repository.TicketRepository;
import com.auhpp.event_management.service.CloudinaryService;
import com.auhpp.event_management.service.EventService;
import com.auhpp.event_management.service.EventSessionService;
import com.auhpp.event_management.service.EventStaffService;
import com.auhpp.event_management.util.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    EventRepository eventRepository;
    EventMapper eventMapper;
    CloudinaryService cloudinaryService;
    EventSessionService eventSessionService;
    AppUserRepository appUserRepository;
    CategoryRepository categoryRepository;
    EventStaffService eventStaffService;
    TicketRepository ticketRepository;

    @Override
    @Transactional
    public EventResponse createEvent(EventCreateRequest eventCreateRequest, MultipartFile thumbnail) {
        Event event = eventMapper.toEvent(eventCreateRequest);
        String email = SecurityUtils.getCurrentUserLogin();

        AppUser appUser = appUserRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        event.setAppUser(appUser);

        Category category = categoryRepository.findById(eventCreateRequest.getCategoryId()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        event.setCategory(category);

        // handle geometry location data
        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate locationCoordinate = new Coordinate(eventCreateRequest.getLocationLongitude(),
                eventCreateRequest.getLocationLatitude());
        Point locationPoint = geometryFactory.createPoint(locationCoordinate);
        event.setLocationCoordinates(locationPoint);

        event.setStatus(EventStatus.PENDING);
        eventRepository.save(event);

        // handle event staff
        eventStaffService.createEventStaff(EventStaffCreateRequest.builder()
                .eventId(event.getId())
                .roleName(RoleName.EVENT_OWNER)
                .userId(appUser.getId())
                .build());

        // upload thumbnail
        Map<String, Object> uploadResult = cloudinaryService.uploadFile(thumbnail,
                FolderName.EVENT.getValue() + email + "/" + event.getId() + "/thumbnail");
        String publicId = (String) uploadResult.get("public_id");
        String imageUrl = (String) uploadResult.get("secure_url");
        event.setThumbnail(imageUrl);
        event.setThumbnailPublicId(publicId);

        eventRepository.save(event);

        for (EventSessionCreateRequest eventSessionCreateRequest : eventCreateRequest.getEventSessionCreateRequests()) {
            eventSessionService.createEventSession(eventSessionCreateRequest, event.getId());
        }
        return eventMapper.toEventResponse(event);
    }

    @Override
    @Transactional
    public void approveEvent(Long id, EventApproveRequest eventApproveRequest) {
        Event event = eventRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (event.getStatus() == EventStatus.PENDING) {
            event.setStatus(EventStatus.APPROVED);

            event.setCommissionRate(eventApproveRequest.getCommissionRate());
            event.setCommissionFixedPerTicket(eventApproveRequest.getCommissionFixedPerTicket());
            List<Ticket> tickets = event.getEventSessions().stream().flatMap(
                    eventSession -> eventSession.getTickets().stream().peek(
                            ticket -> ticket.setStatus(TicketStatus.ACTIVE)
                    )
            ).toList();
            ticketRepository.saveAll(tickets);
            eventRepository.save(event);

        } else {
            throw new AppException(ErrorCode.RESOURCE_CAN_NOT_UPDATE);
        }
    }

    @Override
    @Transactional
    public void rejectEvent(Long id, RejectionRequest rejectionRequest) {
        Event event = eventRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (event.getStatus() == EventStatus.PENDING) {
            event.setStatus(EventStatus.REJECTED);
            event.setRejectionReason(rejectionRequest.getReason());
            eventRepository.save(event);
        } else {
            throw new AppException(ErrorCode.RESOURCE_CAN_NOT_UPDATE);
        }
    }

    @Override
    public PageResponse<EventResponse> getEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC,
                "createdAt"));
        Page<Event> pageData = eventRepository.findAll(pageable);
        List<EventResponse> eventResponses = pageData.getContent().stream().map(
                eventMapper::toEventResponse
        ).toList();
        return PageResponse.<EventResponse>builder()
                .currentPage(page)
                .totalElements(pageData.getTotalElements())
                .totalPage(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .data(eventResponses)
                .build();
    }

    @Override
    public PageResponse<EventResponse> getEventsByUser(int page, int size) {
        String email = SecurityUtils.getCurrentUserLogin();
        AppUser user = appUserRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC,
                "createdAt"));
        Page<Event> pageData = eventRepository.findAllByUserId(user.getId(), pageable);
        List<EventResponse> eventResponses = pageData.getContent().stream().map(
                eventMapper::toEventResponse
        ).toList();
        return PageResponse.<EventResponse>builder()
                .currentPage(page)
                .totalElements(pageData.getTotalElements())
                .totalPage(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .data(eventResponses)
                .build();
    }

    @Override
    public EventResponse getEventById(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        return eventMapper.toEventResponse(event);
    }
}

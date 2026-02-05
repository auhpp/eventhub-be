package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.*;
import com.auhpp.event_management.dto.request.*;
import com.auhpp.event_management.dto.response.EventBasicResponse;
import com.auhpp.event_management.dto.response.EventResponse;
import com.auhpp.event_management.dto.response.EventSearchRequest;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.entity.AppUser;
import com.auhpp.event_management.entity.Category;
import com.auhpp.event_management.entity.Event;
import com.auhpp.event_management.entity.Ticket;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.EventBasicMapper;
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
import com.auhpp.event_management.util.SpecBuilder;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;


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
    EventBasicMapper eventBasicMapper;

    @Override
    @Transactional
    public EventResponse createEvent(EventCreateRequest eventCreateRequest, MultipartFile thumbnail,
                                     MultipartFile poster) {
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
        if (eventCreateRequest.getType() == EventType.OFFLINE) {
            GeometryFactory geometryFactory = new GeometryFactory();
            Coordinate locationCoordinate = new Coordinate(eventCreateRequest.getLocationLongitude(),
                    eventCreateRequest.getLocationLatitude());
            Point locationPoint = geometryFactory.createPoint(locationCoordinate);
            event.setLocationCoordinates(locationPoint);
        }

        event.setStatus(EventStatus.PENDING);
        eventRepository.save(event);

        // handle event staff
        eventStaffService.createEventStaff(EventStaffCreateRequest.builder()
                .eventId(event.getId())
                .roleName(RoleName.EVENT_OWNER)
                .emails(Set.of(appUser.getEmail()))
                .build());

        // upload thumbnail
        Map<String, Object> uploadResult = cloudinaryService.uploadFile(thumbnail,
                FolderName.EVENT.getValue() + email + "/" + event.getId() + "/thumbnail");
        String publicId = (String) uploadResult.get("public_id");
        String imageUrl = (String) uploadResult.get("secure_url");
        event.setThumbnail(imageUrl);
        event.setThumbnailPublicId(publicId);

        // upload poster
        Map<String, Object> uploadPosterResult = cloudinaryService.uploadFile(poster,
                FolderName.EVENT.getValue() + email + "/" + event.getId() + "/poster");
        String posterPublicId = (String) uploadPosterResult.get("public_id");
        String posterUrl = (String) uploadPosterResult.get("secure_url");
        event.setPoster(posterUrl);
        event.setPosterPublicId(posterPublicId);

        eventRepository.save(event);

        for (EventSessionCreateRequest eventSessionCreateRequest : eventCreateRequest.getEventSessionCreateRequests()) {
            eventSessionService.createEventSession(eventSessionCreateRequest, event.getId());
        }
        return eventMapper.toEventResponse(event);
    }


    @Override
    @Transactional
    public EventBasicResponse updateEvent(Long id, EventUpdateRequest request,
                                          MultipartFile thumbnail, MultipartFile poster) {

        Event event = eventRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (event.isExpired()) {
            throw new AppException(ErrorCode.RESOURCE_CAN_NOT_UPDATE);
        }
        eventMapper.updateEventFromRequest(request, event);
        if (event.getType() == EventType.OFFLINE && request.getLocationLatitude() != null
                && request.getLocationLongitude() != null) {
            GeometryFactory geometryFactory = new GeometryFactory();
            Coordinate locationCoordinate = new Coordinate(request.getLocationLongitude(),
                    request.getLocationLatitude());
            Point locationPoint = geometryFactory.createPoint(locationCoordinate);
            event.setLocationCoordinates(locationPoint);
        }

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(
                    () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
            );
            event.setCategory(category);
        }
        AppUser owner = event.getAppUser();
        Map<String, Object> uploadResult;
        String publicId;
        String imageUrl;
        if (thumbnail != null) {
            //delete old thumbnail
            cloudinaryService.deleteFile(event.getThumbnailPublicId());
            // upload thumbnail
            uploadResult = cloudinaryService.uploadFile(thumbnail,
                    FolderName.EVENT.getValue() + owner.getEmail() + "/" + event.getId() + "/thumbnail");
            publicId = (String) uploadResult.get("public_id");
            imageUrl = (String) uploadResult.get("secure_url");
            event.setThumbnail(imageUrl);
            event.setThumbnailPublicId(publicId);
        }
        if (poster != null) {
            // delete old poster
            cloudinaryService.deleteFile(event.getPosterPublicId());
            // upload poster
            uploadResult = cloudinaryService.uploadFile(poster,
                    FolderName.EVENT.getValue() + owner.getEmail() + "/" + event.getId() + "/poster");
            publicId = (String) uploadResult.get("public_id");
            imageUrl = (String) uploadResult.get("secure_url");
            event.setPoster(imageUrl);
            event.setPosterPublicId(publicId);
        }
        eventRepository.save(event);
        return eventBasicMapper.toEventBasicResponse(event);
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
    public PageResponse<EventResponse> getEvents(EventSearchRequest request, int page, int size) {
        Specification<Event> spec = Specification.allOf();
        if (request.getStatus() != null) {
            spec.and(SpecBuilder.create("status", "=", request.getStatus()));
        }
        if (request.getUserId() != null) {
            spec.and(SpecBuilder.create("id", "=", request.getUserId(), "appUser"));
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC,
                "createdAt"));
        Page<Event> pageData = eventRepository.findAll(spec, pageable);
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

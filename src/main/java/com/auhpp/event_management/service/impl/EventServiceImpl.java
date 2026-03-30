package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.*;
import com.auhpp.event_management.dto.request.*;
import com.auhpp.event_management.dto.response.EventBasicResponse;
import com.auhpp.event_management.dto.response.EventResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.TagResponse;
import com.auhpp.event_management.entity.*;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.EventBasicMapper;
import com.auhpp.event_management.mapper.EventMapper;
import com.auhpp.event_management.repository.*;
import com.auhpp.event_management.service.*;
import com.auhpp.event_management.util.SecurityUtils;
import com.auhpp.event_management.util.SlugUtils;
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

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;


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
    BookingRepository bookingRepository;
    AttendeeRepository attendeeRepository;
    EventSeriesRepository eventSeriesRepository;
    TagRepository tagRepository;
    TagService tagService;

    private void handleEventTags(List<EventTag> eventTags, List<TagUpdateRequest> requests, Event event) {
        for (TagUpdateRequest tagRequest : requests) {
            Tag tag;
            if (tagRequest.getId() != null) {
                tag = tagRepository.findById(tagRequest.getId()).orElseThrow(
                        () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
                );
            } else {
                String slug = SlugUtils.toSlug(tagRequest.getName());
                // check exists
                Optional<Tag> existsTag = tagRepository.findBySlug(slug);
                if (existsTag.isPresent()) {
                    tag = existsTag.get();
                } else {
                    TagResponse response = tagService.create(TagCreateRequest.builder()
                            .name(tagRequest.getName())
                            .type(TagType.CUSTOM)
                            .build());
                    tag = tagRepository.findById(response.getId()).orElseThrow(
                            () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
                    );
                }
            }
            eventTags.add(EventTag.builder()
                    .event(event)
                    .tag(tag)
                    .build());
        }
    }

    @Override
    @Transactional
    public EventResponse createEvent(EventCreateRequest request, MultipartFile thumbnail,
                                     MultipartFile poster) {
        Event event = eventMapper.toEvent(request);
        String email = SecurityUtils.getCurrentUserLogin();

        AppUser appUser = appUserRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        event.setAppUser(appUser);

        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        event.setCategory(category);

        if (request.getEventSeriesId() != null) {
            EventSeries eventSeries = eventSeriesRepository.findById(request.getEventSeriesId()).orElseThrow(
                    () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
            );
            event.setEventSeries(eventSeries);
        }

        // handle geometry location data
        if (request.getType() == EventType.OFFLINE) {
            GeometryFactory geometryFactory = new GeometryFactory();
            Coordinate locationCoordinate = new Coordinate(request.getLocationLongitude(),
                    request.getLocationLatitude());
            Point locationPoint = geometryFactory.createPoint(locationCoordinate);
            event.setLocationCoordinates(locationPoint);
        }

        event.setStatus(EventStatus.PENDING);
        eventRepository.save(event);

        // handle tags
        List<EventTag> eventTags = new ArrayList<>();
        handleEventTags(eventTags, request.getTags(), event);
        event.setEventTags(eventTags);

        // handle event staff
        eventStaffService.createEventStaff(EventStaffCreateRequest.builder()
                .eventId(event.getId())
                .roleName(RoleName.EVENT_OWNER)
                .emails(Set.of(appUser.getEmail()))
                .build());

        // upload thumbnail
        Map<String, Object> uploadResult = cloudinaryService.uploadFile(thumbnail,
                FolderName.EVENT.getValue() + email + "/" + event.getId() + "/thumbnail", true);
        String publicId = (String) uploadResult.get("public_id");
        String imageUrl = (String) uploadResult.get("secure_url");
        event.setThumbnail(imageUrl);
        event.setThumbnailPublicId(publicId);

        // upload poster
        Map<String, Object> uploadPosterResult = cloudinaryService.uploadFile(poster,
                FolderName.EVENT.getValue() + email + "/" + event.getId() + "/poster", true);
        String posterPublicId = (String) uploadPosterResult.get("public_id");
        String posterUrl = (String) uploadPosterResult.get("secure_url");
        event.setPoster(posterUrl);
        event.setPosterPublicId(posterPublicId);

        eventRepository.save(event);

        for (EventSessionCreateRequest eventSessionCreateRequest :
                request.getEventSessionCreateRequests()) {
            eventSessionCreateRequest.setStatus(EventSessionStatus.PENDING);
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
//        if (event.isExpired()) {
//            throw new AppException(ErrorCode.RESOURCE_CAN_NOT_UPDATE);
//        }
        eventMapper.updateEventFromRequest(request, event);
        if (request.getEventSeriesId() != null) {
            EventSeries eventSeries = eventSeriesRepository.findById(request.getEventSeriesId()).orElseThrow(
                    () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
            );
            event.setEventSeries(eventSeries);
        }

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
                    FolderName.EVENT.getValue() + owner.getEmail() + "/" + event.getId() + "/thumbnail", true);
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
                    FolderName.EVENT.getValue() + owner.getEmail() + "/" + event.getId() + "/poster", true);
            publicId = (String) uploadResult.get("public_id");
            imageUrl = (String) uploadResult.get("secure_url");
            event.setPoster(imageUrl);
            event.setPosterPublicId(publicId);
        }

        // handle tag
        if (event.getEventTags() != null) {
            event.getEventTags().clear();
        } else {
            event.setEventTags(new ArrayList<>());
        }
        List<EventTag> eventTags = event.getEventTags();
        handleEventTags(eventTags, request.getTags(), event);
        event.setEventTags(eventTags);

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

            List<EventSession> eventSessions = event.getEventSessions();
            for (EventSession es : eventSessions) {
                es.setStatus(EventSessionStatus.APPROVED);
            }

            List<Ticket> tickets = eventSessions.stream().flatMap(
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

            List<EventSession> eventSessions = event.getEventSessions();
            for (EventSession es : eventSessions) {
                es.setStatus(EventSessionStatus.REJECTED);
            }

            eventRepository.save(event);
        } else {
            throw new AppException(ErrorCode.RESOURCE_CAN_NOT_UPDATE);
        }
    }

    @Override
    public PageResponse<EventResponse> getEvents(EventSearchRequest request, int page, int size) {
        Sort.Direction direction = Sort.Direction.DESC;
        if (request.getSortType() != null && request.getSortType() == SortType.OLDEST) {
            direction = Sort.Direction.ASC;
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(direction,
                "createdAt"));
        Page<Event> pageData = null;
        if (request.getEventSearchStatus() != null) {
            if (request.getEventSearchStatus() == EventSearchStatus.COMING) {
                pageData = eventRepository.findAllByUserIdAndStatusAndComingStatus(request.getUserId(),
                        request.getStatus(), LocalDateTime.now(), request.getType(),
                        request.getEventSeriesId(), request.getFromDate(), request.getToDate(), request.getName(),
                        pageable);
            } else if (request.getEventSearchStatus() == EventSearchStatus.PAST) {
                pageData = eventRepository.findAllByUserIdAndStatusAndPastStatus(request.getUserId(),
                        request.getStatus(), LocalDateTime.now(), request.getType(),
                        request.getEventSeriesId(), request.getFromDate(), request.getToDate(), request.getName(),
                        pageable);
            }
        } else {
            LocalDateTime filterStartDate = request.getFromDate();
            LocalDateTime filterEndDate = request.getToDate();
            LocalDateTime now = LocalDateTime.now();
            if (request.getThisWeek() != null && request.getThisWeek()) {
                filterStartDate = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                        .truncatedTo(ChronoUnit.DAYS);
                filterEndDate = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                        .withHour(23).withMinute(59).withSecond(59);
            } else if (request.getThisMonth() != null && request.getThisMonth()) {
                filterStartDate = now.with(TemporalAdjusters.firstDayOfMonth())
                        .truncatedTo(ChronoUnit.DAYS);
                filterEndDate = now.with(TemporalAdjusters.lastDayOfMonth())
                        .withHour(23).withMinute(59).withSecond(59);
            }
            pageData = eventRepository.filterEvents(request.getUserId(), request.getStatus(),
                    request.getType(), filterStartDate, filterEndDate, request.getCategoryIds(),
                    request.getPriceFrom(), request.getPriceTo(), request.getName(), request.getEventSeriesId(),
                    request.getHasResale(),
                    request.getCurrentUserId(),
                    request.getHasFavorite(),
                    request.getEmail(),
                    pageable);
        }
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
        EventResponse response = eventMapper.toEventResponse(event);
        response.setHasPhotos(!event.getEventImages().isEmpty());
        return response;
    }

    @Override
    @Transactional
    public void cancelEvent(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        String email = SecurityUtils.getCurrentUserLogin();
        if (!Objects.equals(email, event.getAppUser().getEmail())) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        if (event.getStatus() == EventStatus.PENDING) {
            event.setStatus(EventStatus.CANCELLED);
            eventRepository.save(event);
        } else if (event.getStatus() == EventStatus.APPROVED) {
            if (event.isOnGoing()) {
                throw new AppException(ErrorCode.EVENT_ON_GOING);
            }
            event.setStatus(EventStatus.CANCELLED);
            eventRepository.save(event);

            List<Booking> bookings = bookingRepository.findAllByEventId(event.getId(), BookingStatus.PAID, null);
            for (Booking booking : bookings) {
                booking.setStatus(BookingStatus.CANCELLED_BY_EVENT);
            }
            bookingRepository.saveAll(bookings);

            List<Attendee> attendees = attendeeRepository.findAllByEventId(event.getId(), AttendeeStatus.VALID);
            for (Attendee attendee : attendees) {
                attendee.setStatus(AttendeeStatus.CANCELLED_BY_EVENT);
            }
            attendeeRepository.saveAll(attendees);
        }
    }

    @Override
    public Integer countEvent(EventCountRequest request) {
        return eventRepository.countEvent(request.getCategoryId(), request.getStatuses(), null, null);
    }


}

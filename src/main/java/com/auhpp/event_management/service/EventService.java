package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.EventApproveRequest;
import com.auhpp.event_management.dto.request.EventCreateRequest;
import com.auhpp.event_management.dto.request.EventUpdateRequest;
import com.auhpp.event_management.dto.request.RejectionRequest;
import com.auhpp.event_management.dto.response.EventBasicResponse;
import com.auhpp.event_management.dto.response.EventResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.entity.Event;
import org.springframework.web.multipart.MultipartFile;

public interface EventService {
    EventResponse createEvent(EventCreateRequest eventCreateRequest, MultipartFile thumbnail,
                              MultipartFile poster);

    EventBasicResponse updateEvent(Long id, EventUpdateRequest request, MultipartFile thumbnail,
                                   MultipartFile poster);

    void approveEvent(Long id, EventApproveRequest eventApproveRequest);

    void rejectEvent(Long id, RejectionRequest rejectionRequest);

    PageResponse<EventResponse> getEvents(int page, int size);

    PageResponse<EventResponse> getEventsByUser(int page, int size);

    EventResponse getEventById(Long id);

}

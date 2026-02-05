package com.auhpp.event_management.controller;

import com.auhpp.event_management.dto.request.*;
import com.auhpp.event_management.dto.response.*;
import com.auhpp.event_management.service.EventService;
import com.auhpp.event_management.service.EventSessionService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/event")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventController {

    EventService eventService;
    EventSessionService eventSessionService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<EventResponse> createEvent(
            @RequestPart(value = "data") @Valid EventCreateRequest eventCreateRequest,
            @RequestPart(value = "thumbnail") MultipartFile thumbnail,
            @RequestPart(value = "poster") MultipartFile poster

    ) {
        EventResponse result = eventService.createEvent(eventCreateRequest, thumbnail, poster);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping("/approve/{eventId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> approveEvent(
            @PathVariable("eventId") Long id,
            @Valid @RequestBody EventApproveRequest eventApproveRequest
    ) {
        eventService.approveEvent(id, eventApproveRequest);
        return ResponseEntity
                .status(HttpStatus.OK).build();
    }

    @PostMapping("/reject/{eventId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> rejectEvent(
            @PathVariable("eventId") Long id,
            @Valid @RequestBody RejectionRequest rejectionRequest
    ) {
        eventService.rejectEvent(id, rejectionRequest);
        return ResponseEntity
                .status(HttpStatus.OK).build();
    }

    @PostMapping("/filter")
    public ResponseEntity<PageResponse<EventResponse>> getEvents(
            @RequestBody EventSearchRequest request,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<EventResponse> response = eventService.getEvents(request, page, size);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEventById(
            @PathVariable(name = "eventId") Long id
    ) {
        EventResponse response = eventService.getEventById(id);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/{eventId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<EventBasicResponse> updateEvent(
            @PathVariable(name = "eventId") Long id,
            @RequestPart(value = "data") @Valid EventUpdateRequest request,
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestPart(value = "poster", required = false) MultipartFile poster
    ) {
        EventBasicResponse response = eventService.updateEvent(id, request, thumbnail, poster);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }


    @PostMapping("/{eventId}/event-session")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<EventSessionResponse> createEventSession(
            @PathVariable(name = "eventId") Long id,
            @Valid @RequestBody EventSessionCreateRequest request
    ) {
        EventSessionResponse response = eventSessionService.createEventSession(request, id);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

}

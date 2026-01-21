package com.auhpp.event_management.controller;

import com.auhpp.event_management.dto.request.EventApproveRequest;
import com.auhpp.event_management.dto.request.EventCreateRequest;
import com.auhpp.event_management.dto.request.RejectionRequest;
import com.auhpp.event_management.dto.response.EventResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.service.EventService;
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<EventResponse> createEvent(
            @RequestPart(value = "data") @Valid EventCreateRequest eventCreateRequest,
            @RequestPart(value = "thumbnail") MultipartFile thumbnail
    ) {
        EventResponse result = eventService.createEvent(eventCreateRequest, thumbnail);
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

    @GetMapping("/current-user")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<PageResponse<EventResponse>> getEventsByUser(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<EventResponse> response = eventService.getEventsByUser(page, size);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<PageResponse<EventResponse>> getEvents(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<EventResponse> response = eventService.getEvents(page, size);
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

}

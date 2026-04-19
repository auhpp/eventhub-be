package com.auhpp.event_management.controller;

import com.auhpp.event_management.dto.request.EventSeriesCreateRequest;
import com.auhpp.event_management.dto.request.EventSeriesSearchRequest;
import com.auhpp.event_management.dto.request.EventSeriesUpdateRequest;
import com.auhpp.event_management.dto.response.EventSeriesResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.service.EventSeriesService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/event-series")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventSeriesController {

    EventSeriesService eventSeriesService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<EventSeriesResponse> createEventSeries(
            @Valid @ModelAttribute EventSeriesCreateRequest request
    ) {
        EventSeriesResponse result = eventSeriesService.createEventSeries(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<Void> deleteEventSeries(
            @PathVariable("id") Long id
    ) {
        eventSeriesService.deleteEventSeries(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<EventSeriesResponse> updateEventSeries(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute EventSeriesUpdateRequest request
    ) {
        EventSeriesResponse result = eventSeriesService.updateEventSeries(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping("/filter")
    public ResponseEntity<PageResponse<EventSeriesResponse>> getEventSeries(
            @RequestBody EventSeriesSearchRequest request,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<EventSeriesResponse> result = eventSeriesService.getEventSeries(request, page, size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventSeriesResponse> getEventSeriesById(
            @PathVariable("id") Long id
    ) {
        EventSeriesResponse result = eventSeriesService.getEventSeriesById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<EventSeriesResponse>> getAllEventSeries(
    ) {
        List<EventSeriesResponse> result = eventSeriesService.getAllEventSeries();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }
}

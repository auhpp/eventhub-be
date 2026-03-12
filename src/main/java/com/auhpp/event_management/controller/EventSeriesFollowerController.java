package com.auhpp.event_management.controller;

import com.auhpp.event_management.dto.request.EventSeriesFollowerCreateRequest;
import com.auhpp.event_management.dto.request.EventSeriesFollowerSearchRequest;
import com.auhpp.event_management.dto.response.EventSeriesFollowerResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.service.EventSeriesFollowerService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/event-series-follower")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventSeriesFollowerController {
    EventSeriesFollowerService esFollowerService;

    @PostMapping
    public ResponseEntity<EventSeriesFollowerResponse> createEventSeriesFollower(
            @Valid @RequestBody EventSeriesFollowerCreateRequest request
    ) {
        EventSeriesFollowerResponse result = esFollowerService.createEventSeriesFollower(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventSeriesFollower(
            @PathVariable("id") Long id
    ) {
        esFollowerService.deleteEventSeriesFollower(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/filter")
    public ResponseEntity<PageResponse<EventSeriesFollowerResponse>> getEventSeries(
            EventSeriesFollowerSearchRequest request,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<EventSeriesFollowerResponse> result = esFollowerService.getEventSeriesFollowers(
                request, page, size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping("/count")
    public ResponseEntity<Integer> countEventSeriesFollowers(
            @RequestBody EventSeriesFollowerSearchRequest request
    ) {
        Integer result = esFollowerService.countEventSeriesFollowers(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }
}

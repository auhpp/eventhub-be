package com.auhpp.event_management.controller;

import com.auhpp.event_management.constant.ProcessStatus;
import com.auhpp.event_management.dto.response.EventImageResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.service.EventImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/event-image")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventImageController {
    EventImageService eventImageService;

    @PostMapping(path = "/{eventSessionId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<List<EventImageResponse>> uploadEventImages(
            @PathVariable(name = "eventSessionId") Long eventSessionId,
            @RequestParam(name = "files") List<MultipartFile> files
    ) {
        List<EventImageResponse> result = eventImageService.uploadEventImages(
                eventSessionId, files
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @GetMapping("/filter/{eventSessionId}")
    public ResponseEntity<PageResponse<EventImageResponse>> findAll(
            @PathVariable(name = "eventSessionId") Long eventSessionId,
            @RequestParam(name = "processStatus", required = false) ProcessStatus status,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<EventImageResponse> result = eventImageService.findAll(eventSessionId, status, page, size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping("/search/{eventSessionId}")
    public ResponseEntity<List<EventImageResponse>> searchPhotos(
            @PathVariable(name = "eventSessionId") Long eventSessionId,
            @RequestParam(name = "file") MultipartFile selfie
    ) {
        List<EventImageResponse> result = eventImageService.searchPhotos(
                eventSessionId, selfie
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping(path = "/refresh/{eventSessionId}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<Void> refreshProcessImages(
            @PathVariable(name = "eventSessionId") Long eventSessionId) {
        eventImageService.refreshProcessImages(
                eventSessionId
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping(path = "/{imageId}")
    public ResponseEntity<Void> delete(
            @PathVariable(name = "imageId") Long imageId) {
        eventImageService.deleteImage(
                imageId
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}

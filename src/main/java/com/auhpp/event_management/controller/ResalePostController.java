package com.auhpp.event_management.controller;

import com.auhpp.event_management.dto.request.RejectionRequest;
import com.auhpp.event_management.dto.request.ResalePostCreateRequest;
import com.auhpp.event_management.dto.request.ResalePostSearchRequest;
import com.auhpp.event_management.dto.request.ResalePostUpdateRequest;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.ResalePostResponse;
import com.auhpp.event_management.service.ResalePostService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resale-post")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResalePostController {
    ResalePostService resalePostService;

    @PostMapping
    public ResponseEntity<ResalePostResponse> create(
            @Valid @RequestBody ResalePostCreateRequest request
    ) {
        ResalePostResponse result = resalePostService.create(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResalePostResponse> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody ResalePostUpdateRequest request
    ) {
        ResalePostResponse result = resalePostService.update(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> approve(
            @PathVariable("id") Long id
    ) {
        resalePostService.approvePost(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/{id}/cancel-by-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> cancelPostByAdmin(
            @PathVariable("id") Long id,
            @Valid @RequestBody RejectionRequest request
    ) {
        resalePostService.cancelPostByAdmin(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> rejectPost(
            @PathVariable("id") Long id,
            @Valid @RequestBody RejectionRequest request
    ) {
        resalePostService.rejectPost(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelPost(
            @PathVariable("id") Long id
    ) {
        resalePostService.cancelPost(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/filter")
    public ResponseEntity<PageResponse<ResalePostResponse>> filter(
            @RequestBody ResalePostSearchRequest request,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<ResalePostResponse> response = resalePostService.filter(request, page, size);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResalePostResponse> getById(
            @PathVariable("id") Long id
    ) {
        ResalePostResponse response = resalePostService.getById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}

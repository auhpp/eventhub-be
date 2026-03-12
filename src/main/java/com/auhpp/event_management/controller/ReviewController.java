package com.auhpp.event_management.controller;

import com.auhpp.event_management.dto.request.ReviewCreateRequest;
import com.auhpp.event_management.dto.request.ReviewSearchRequest;
import com.auhpp.event_management.dto.request.ReviewUpdateRequest;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.ReviewResponse;
import com.auhpp.event_management.dto.response.ReviewStatsResponse;
import com.auhpp.event_management.service.ReviewService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewController {
    ReviewService reviewService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReviewResponse> createReview(
            @Valid @ModelAttribute ReviewCreateRequest request
    ) {
        ReviewResponse result = reviewService.createReview(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping(path = "/{reviewId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable("reviewId") Long reviewId,
            @Valid @ModelAttribute ReviewUpdateRequest request
    ) {
        ReviewResponse result = reviewService.updateReview(reviewId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }


    @PostMapping("/filter")
    public ResponseEntity<PageResponse<ReviewResponse>> getReviews(
            @RequestBody ReviewSearchRequest request,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<ReviewResponse> result = reviewService.getReviews(request, page, size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @GetMapping("/stats/{eventSessionId}")
    public ResponseEntity<ReviewStatsResponse> getReviewStats(
            @PathVariable(name = "eventSessionId") Long eventSessionId
    ) {
        ReviewStatsResponse result = reviewService.getReviewStats(eventSessionId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }
}

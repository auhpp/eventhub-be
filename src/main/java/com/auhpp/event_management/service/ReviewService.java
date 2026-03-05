package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.ReviewCreateRequest;
import com.auhpp.event_management.dto.request.ReviewSearchRequest;
import com.auhpp.event_management.dto.request.ReviewUpdateRequest;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.ReviewResponse;

public interface ReviewService {
    ReviewResponse createReview(ReviewCreateRequest request);

    ReviewResponse updateReview(Long id, ReviewUpdateRequest request);

    PageResponse<ReviewResponse> getReviews(ReviewSearchRequest request, int page, int size);

}

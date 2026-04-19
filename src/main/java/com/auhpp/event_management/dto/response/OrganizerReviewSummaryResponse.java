package com.auhpp.event_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizerReviewSummaryResponse {
    private List<CountRatingResponse> ratings;
    private int totalReview;
    private double averageRating;


}


package com.auhpp.event_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewStatsResponse {
    private Double averageRating;

    private Long totalReviews;

    private List<RatingBreakdownResponse> breakdown;
}

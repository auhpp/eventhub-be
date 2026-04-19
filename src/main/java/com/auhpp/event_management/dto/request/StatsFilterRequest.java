package com.auhpp.event_management.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatsFilterRequest {
    private Long eventSessionId;

    private Long eventSeriesId;

    private Long organizerId;

    private DateRangeFilterRequest dateRangeFilter;

    private PaginationFilterRequest paginationRequest;
}

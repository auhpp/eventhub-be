package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.SortBy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DateRangeFilterRequest {
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private SortBy sortBy;
}

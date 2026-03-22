package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.EventSearchStatus;
import com.auhpp.event_management.constant.EventStatus;
import com.auhpp.event_management.constant.EventType;
import com.auhpp.event_management.constant.SortType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventSearchRequest {
    private String name;
    private EventStatus status;
    private Long userId;
    private EventSearchStatus eventSearchStatus;
    private EventType type;
    private List<Long> categoryIds;
    private Boolean thisWeek;
    private Boolean thisMonth;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private Double priceFrom;
    private Double priceTo;
    private SortType sortType;
    private Long eventSeriesId;
    private Boolean hasResale;

}

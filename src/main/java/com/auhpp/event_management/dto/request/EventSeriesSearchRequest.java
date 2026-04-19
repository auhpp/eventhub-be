package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.EventSeriesStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventSeriesSearchRequest {
    private Long userId;
    private Long userFollowerId;
    private List<EventSeriesStatus> statuses;
    private String name;

}

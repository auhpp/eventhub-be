package com.auhpp.event_management.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventSeriesSearchRequest {
    private Long userId;
    private Long userFollowerId;

}

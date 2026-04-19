package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventCountRequest {
    private Long categoryId;

    private Long organizerId;

    private List<EventStatus> statuses;
}

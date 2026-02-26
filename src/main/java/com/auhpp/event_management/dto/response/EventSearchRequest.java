package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.EventSearchStatus;
import com.auhpp.event_management.constant.EventStatus;
import com.auhpp.event_management.constant.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventSearchRequest {
    private EventStatus status;
    private Long userId;
    private EventSearchStatus eventSearchStatus;
    private EventType type;
}

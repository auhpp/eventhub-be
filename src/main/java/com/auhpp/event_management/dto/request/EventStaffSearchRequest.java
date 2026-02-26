package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.EventStaffStatus;
import com.auhpp.event_management.validation.annotation.ValidCreateEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidCreateEvent
public class EventStaffSearchRequest {
    private String email;
    private EventStaffStatus status;
    private Long eventId;
}

package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.AttendeeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckinSearchRequest {
    private AttendeeStatus status;
    private Long eventSessionId;
}

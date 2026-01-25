package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.AttendeeStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendeeSearchRequest {
    @NotNull(message = "Attendee status cannot be null")
    private AttendeeStatus status;
}

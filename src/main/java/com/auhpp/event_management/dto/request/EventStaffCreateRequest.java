package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.RoleName;
import com.auhpp.event_management.validation.annotation.ValidCreateEvent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidCreateEvent
public class EventStaffCreateRequest {
    @NotNull(message = "User id cannot be null")
    private Long userId;

    @NotNull(message = "Event id cannot be null")
    private Long eventId;

    @NotNull(message = "Event role name cannot be null")
    private RoleName roleName;
}

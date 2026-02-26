package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.InvitationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventInvitationSearchRequest {
    private InvitationStatus status;

    @NotNull(message = "Event session id cannot be null")
    private Long eventSessionId;
}

package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.ActionType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInRequest {
    private Long attendeeId;

    @NotEmpty(message = "Ticket code cannot empty")
    private String ticketCode;

    @NotNull(message = "Event id cannot null")
    private Long eventId;

    @NotNull(message = "Action type cannot null")
    private ActionType actionType;

}

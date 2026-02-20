package com.auhpp.event_management.dto.request;

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
    @NotEmpty(message = "Ticket code cannot empty")
    private String ticketCode;

    @NotNull(message = "Event id cannot null")
    private Long eventId;
}

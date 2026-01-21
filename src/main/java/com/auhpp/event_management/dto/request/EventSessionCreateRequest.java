package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.validation.annotation.ValidCreateEvent;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventSessionCreateRequest {

    @NotNull(message = "Event session start date time cannot be null")
    private LocalDateTime startDateTime;

    @NotNull(message = "Event session end date time cannot be null")
    private LocalDateTime endDateTime;

    @NotEmpty(message = "Event session ticket cannot be empty")
    @Size(min = 1)
    private List<@Valid TicketCreateRequest> ticketCreateRequests;

}

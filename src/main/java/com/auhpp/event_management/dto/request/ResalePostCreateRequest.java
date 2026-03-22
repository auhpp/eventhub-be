package com.auhpp.event_management.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResalePostCreateRequest {
    @NotNull(message = "price per ticket cannot null")
    private Double pricePerTicket;

    @NotNull(message = "Ticket id cannot null")
    private Long ticketId;

    @NotNull(message = "Has retail cannot null")
    private Boolean hasRetail;

    @NotEmpty(message = "Attendees cannot empty")
    @Size(min = 1)
    private List<Long> attendeeIds;

}

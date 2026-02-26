package com.auhpp.event_management.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingTicketRequest {
    @NotNull(message = "Ticket booking quantity cannot be null")
    private Integer quantity;

    @NotNull(message = "Ticket id cannot be null")
    private Long ticketId;
}

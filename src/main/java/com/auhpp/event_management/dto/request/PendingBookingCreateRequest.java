package com.auhpp.event_management.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
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
public class PendingBookingCreateRequest {
    @NotEmpty(message = "Booking ticket cannot be empty")
    @Size(min = 1)
    private List<@Valid BookingTicketRequest> bookingTicketRequests;

}

package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.AttendeeType;
import jakarta.validation.Valid;
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
public class PendingBookingCreateRequest {
    @NotEmpty(message = "Booking ticket cannot be empty")
    @Size(min = 1)
    private List<@Valid BookingTicketRequest> bookingTicketRequests;

    @NotNull(message = "Type cannot be null")
    private AttendeeType type;
}

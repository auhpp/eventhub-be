package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.AttendeeStatus;
import com.auhpp.event_management.constant.AttendeeType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendeeCreateRequest {
    @NotNull(message = "Attendee type cannot be null")
    private AttendeeType type;

    @NotNull(message = "Attendee status cannot be null")
    private AttendeeStatus status;

    @NotNull(message = "Booking id cannot be null")
    private Long bookingId;

    @NotNull(message = "Booking id cannot be null")
    private Long ticketId;
}

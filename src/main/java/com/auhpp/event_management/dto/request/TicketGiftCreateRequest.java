package com.auhpp.event_management.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketGiftCreateRequest {
    @NotNull(message = "Receiver id cannot be null")
    private Long receiverId;

    @NotEmpty(message = "Attendee ids cannot be empty")
    private Set<Long> attendeeIds;

    @NotNull(message = "Booking id cannot be null")
    private Long bookingId;
}

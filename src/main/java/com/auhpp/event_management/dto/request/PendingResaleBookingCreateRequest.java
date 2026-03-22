package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.BookingType;
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
public class PendingResaleBookingCreateRequest {
    @NotEmpty(message = "Attendees cannot empty")
    @Size(min = 1)
    private List<Long> attendeeIds;

    @NotNull(message = "Type cannot be null")
    private BookingType type;

    @NotNull(message = "Resale post id cannot be null")
    private Long resalePostId;
}

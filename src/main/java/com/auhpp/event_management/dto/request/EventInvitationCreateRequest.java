package com.auhpp.event_management.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventInvitationCreateRequest {

    @NotEmpty(message = "Emails cannot be empty")
    @Size(min = 1)
    private Set<String> emails;

    @NotNull(message = "Ticket id cannot be null")
    private Long ticketId;

    private String message;

    private LocalDateTime expiredAt;

    @NotNull(message = "Initial quantity cannot be null")
    private Integer initialQuantity;
}

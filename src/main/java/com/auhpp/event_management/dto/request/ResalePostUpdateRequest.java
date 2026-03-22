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
public class ResalePostUpdateRequest {
    @NotNull(message = "Ticket id cannot null")
    private Long ticketId;

    private Double pricePerTicket;

    private Boolean hasRetail;

}

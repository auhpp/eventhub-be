package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.validation.annotation.ValidCreateEvent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketCreateRequest {


    @NotNull(message = "Ticket price cannot be null")
    private Double price;

    @NotNull(message = "Ticket quantity cannot be null")
    @Min(value = 1)
    private Integer quantity;

    @NotEmpty(message = "Ticket name cannot be empty")
    private String name;

    @NotNull(message = "Ticket open date cannot be null")
    private LocalDateTime openAt;

    @NotNull(message = "Ticket end date cannot be null")
    private LocalDateTime endAt;

    @NotNull(message = "Ticket maximum per purchase cannot be null")
    @Min(value = 1)
    private Integer maximumPerPurchase;
}

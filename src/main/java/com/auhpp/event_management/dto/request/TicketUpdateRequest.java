package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.validation.annotation.ValidUpdateTicket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidUpdateTicket
public class TicketUpdateRequest {
    private String description;

    private Double price;

    private Integer quantity;

    private String name;

    private LocalDateTime openAt;

    private LocalDateTime endAt;

    private Integer maximumPerPurchase;

    private Integer invitationQuota;
}

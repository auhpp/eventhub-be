package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {
    private Long id;

    private Double price;

    private Integer quantity;

    private String name;

    private LocalDateTime openAt;

    private LocalDateTime endAt;

    private Integer maximumPerPurchase;

    private TicketStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

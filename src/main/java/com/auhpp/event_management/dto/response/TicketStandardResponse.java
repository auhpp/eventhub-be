package com.auhpp.event_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketStandardResponse {
    private Long id;

    private Double price;

    private Integer quantity;

    private String name;

    private Integer soldQuantity;

    private EventSessionBasicResponse eventSession;
}

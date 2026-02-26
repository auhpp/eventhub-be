package com.auhpp.event_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketBasicResponse {
    private Long id;

    private String name;

    private Double price;

    private Long eventSessionId;

    private Long eventId;
}

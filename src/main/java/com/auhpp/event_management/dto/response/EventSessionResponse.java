package com.auhpp.event_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventSessionResponse {
    private Long id;

    private String name;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<TicketResponse> tickets;
}

package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.TicketStatus;
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
public class TicketResponse {
    private Long id;

    private Double price;

    private Integer quantity;

    private String name;

    private LocalDateTime openAt;

    private LocalDateTime endAt;

    private Integer maximumPerPurchase;

    private Integer soldQuantity;

    private TicketStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long eventSessionId;

    private Long eventId;

    private Integer invitationQuota;

    private Integer invitedQuantity;

    List <AttendeeBasicResponse> attendees;
}

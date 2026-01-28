package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.InvitationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventInvitationResponse {
    private Long id;

    private InvitationStatus status;

    private String token;

    private String email;

    private String message;

    private Integer initialQuantity;

    private String rejectionMessage;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime expiredAt;

    private BookingBasicResponse booking;

    private UserBasicResponse appUser;

    private TicketBasicResponse ticket;

    private boolean isSendSuccess;

}

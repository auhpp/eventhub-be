package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.TicketGiftStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketGiftResponse {
    private Long id;

    private TicketGiftStatus status;

    private LocalDateTime expiredAt;

    private String rejectionMessage;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private UserBasicResponse sender;

    private UserBasicResponse receiver;

    private BookingBasicResponse booking;

}

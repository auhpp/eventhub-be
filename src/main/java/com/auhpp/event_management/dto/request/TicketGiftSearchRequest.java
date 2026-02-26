package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.TicketGiftStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketGiftSearchRequest {
    private TicketGiftStatus status;
    private Long senderId;
    private Long receiverId;
    private String receiverEmail;
    private String senderEmail;

}

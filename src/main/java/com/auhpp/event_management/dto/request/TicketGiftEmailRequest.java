package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.entity.AppUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketGiftEmailRequest {
    private int ticketQuantity;
    private AppUser sender;
    private String emailReceiver;
    private String eventName;
    private Long ticketGiftId;
    private Long eventId;
}

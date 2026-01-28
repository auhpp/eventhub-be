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
public class InvitationBookingCreateRequest {
    private Long ticketId;

    private Integer quantity;

    private AppUser user;
}

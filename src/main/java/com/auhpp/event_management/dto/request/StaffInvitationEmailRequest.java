package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.RoleName;
import com.auhpp.event_management.entity.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffInvitationEmailRequest {
    private Event event;
    private String message;
    private String recipientEmail;
    private String token;
    private RoleName roleName;

}

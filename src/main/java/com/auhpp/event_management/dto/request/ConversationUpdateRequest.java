package com.auhpp.event_management.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationUpdateRequest {
    private Boolean hasPin;

    private Boolean hasNotification;

}

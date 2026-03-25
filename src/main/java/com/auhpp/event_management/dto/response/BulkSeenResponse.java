package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkSeenResponse {
    private Long conversationId;
    private MessageStatus status;
    private Long messageId;
    private boolean isBulk;
}

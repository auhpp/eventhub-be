package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.CommonStatus;
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
public class ConversationResponse {
    private Long id;

    private Boolean hasPin;

    private Boolean hasNotification;

    private CommonStatus status;

    private LocalDateTime createdAt;

    private MessageResponse latestMessage;

    private List<ConversationMemberResponse> conversationMembers;
}

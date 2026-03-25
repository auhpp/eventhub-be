package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.MessageStatus;
import com.auhpp.event_management.constant.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private Long id;

    private String content;

    private MessageStatus status;

    private String pathUrl;

    private String tempId;

    private MessageType type;

    private LocalDateTime seenAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private UserBasicResponse sender;

    private UserBasicResponse recipient;

    private ReplyMessageResponse replyMessage;

    private Long conversationId;
}

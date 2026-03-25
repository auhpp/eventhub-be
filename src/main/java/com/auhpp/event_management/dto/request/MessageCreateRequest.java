package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.MessageType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageCreateRequest {
    private MultipartFile file;

    private String content;

    @NotNull(message = "Type cannot null")
    private MessageType type;

    private Long replyMessageId;

    private Long conversationId;

    @NotNull(message = "Temp id cannot null")
    private String tempId;

    @NotNull(message = "Recipient ids cannot null")
    private Long recipientId;
}

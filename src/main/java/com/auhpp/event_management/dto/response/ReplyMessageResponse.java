package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyMessageResponse {
    private Long id;

    private String content;

    private String pathUrl;

    private MessageType type;

    private UserBasicResponse sender;
}

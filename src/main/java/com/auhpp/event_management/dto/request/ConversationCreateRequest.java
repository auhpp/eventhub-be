package com.auhpp.event_management.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationCreateRequest {
    private Boolean hasNotification;

    @NotEmpty(message = "Member ids cannot empty")
    @Size(max = 2, min = 2)
    private List<Long> memberIds;

    private Long latestMessageId;
}

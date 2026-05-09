package com.auhpp.event_management.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewReplyRequest {
    @NotEmpty(message = "Reply message cannot be empty")
    private String replyMessage;

    @NotNull(message = "Event staff id cannot be null")
    private Long eventStaffId;
}

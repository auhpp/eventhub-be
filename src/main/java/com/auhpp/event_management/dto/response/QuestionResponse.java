package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.QuestionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponse {
    private Long id;

    private String content;

    private QuestionStatus status;

    private Boolean hasAnonymous;

    private Boolean hasPin;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private int upvoteCount;

    private UserBasicResponse appUser;

    private boolean isUpVoted;

    private Long actionUserId;

    private Long eventSessionId;

}

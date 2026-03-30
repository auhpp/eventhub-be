package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.QuestionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionSearchRequest {
    private Long eventSessionId;

    private Long userId;

    private List<QuestionStatus> statuses;
}

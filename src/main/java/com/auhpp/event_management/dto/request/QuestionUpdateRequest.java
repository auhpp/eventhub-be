package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.QuestionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionUpdateRequest {
    private String content;

    private QuestionStatus status;

    private Boolean hasAnonymous;


}

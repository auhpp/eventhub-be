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
public class QuestionCreateRequest {

    @NotEmpty(message = "Content cannot empty")
    private String content;

    @NotNull(message = "Has anonymous cannot null")
    private Boolean hasAnonymous;

    @NotNull(message = "Event session cannot null")
    private Long eventSessionId;

}

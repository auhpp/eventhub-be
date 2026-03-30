package com.auhpp.event_management.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserNoteCreateRequest {

    private String noteContent;

    private MultipartFile audioFile;

    @NotNull(message = "Question id cannot null")
    private Long questionId;

}

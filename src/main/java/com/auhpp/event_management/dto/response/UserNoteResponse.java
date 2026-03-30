package com.auhpp.event_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserNoteResponse {
    private Long id;

    private String noteContent;

    private String audioUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

package com.auhpp.event_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryFollowerResponse {
    private Long id;
    private CategoryResponse category;
    private UserBasicResponse user;
    private LocalDateTime createdAt;
}

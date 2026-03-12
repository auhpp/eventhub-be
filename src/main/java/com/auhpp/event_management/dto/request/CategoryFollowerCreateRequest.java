package com.auhpp.event_management.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryFollowerCreateRequest {
    @NotNull(message = "User id cannot null")
    private Long userId;

    @NotNull(message = "category id cannot null")
    private Long categoryId;
}

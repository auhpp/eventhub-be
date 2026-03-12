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
public class EventSeriesFollowerCreateRequest {
    @NotNull(message = "User id cannot null")
    private Long userId;

    @NotNull(message = "Event series id cannot null")
    private Long eventSeriesId;
}

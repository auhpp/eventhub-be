package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.AccessImage;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventAccessImageRequest {
    @NotNull(message = "Access image cannot be null")
    private AccessImage accessImage;
}

package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.TagType;
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
public class TagCreateRequest {
    @NotEmpty(message = "Name cannot empty")
    private String name;

    @NotNull(message = "type cannot null")
    private TagType type;
}

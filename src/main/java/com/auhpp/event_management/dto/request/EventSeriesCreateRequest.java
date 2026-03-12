package com.auhpp.event_management.dto.request;

import jakarta.validation.constraints.NotEmpty;
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
public class EventSeriesCreateRequest {

    @NotNull(message = "Avatar cannot null")
    private MultipartFile avatar;

    private MultipartFile coverImage;

    @NotEmpty(message = "Name cannot empty")
    private String name;

    private String description;
}

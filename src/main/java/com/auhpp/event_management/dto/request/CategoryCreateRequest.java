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
public class CategoryCreateRequest {

    @NotEmpty(message = "Category name cannot be empty")
    private String name;

    @NotNull(message = "Category avatar cannot be null")
    private MultipartFile avatar;

    private String description;

}

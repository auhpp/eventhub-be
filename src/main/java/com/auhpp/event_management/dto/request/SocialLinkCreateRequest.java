package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.SocialType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialLinkCreateRequest {
    private Long id;

    @NotNull(message = "Social type cannot be null")
    private SocialType type;

    private String urlLink;
}

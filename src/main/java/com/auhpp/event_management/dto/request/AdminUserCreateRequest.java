package com.auhpp.event_management.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserCreateRequest {
    @NotEmpty(message = "Email cannot empty")
    private String email;

    @NotNull(message = "Expire date time cannot null")
    private LocalDateTime expireDateTime;

    private boolean resend;

}

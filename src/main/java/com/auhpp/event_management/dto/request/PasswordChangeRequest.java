package com.auhpp.event_management.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeRequest {
    @NotEmpty(message = "Old password cannot be empty")
    private String oldPassword;

    @NotEmpty(message = "New password cannot be empty")
    private String newPassword;
}

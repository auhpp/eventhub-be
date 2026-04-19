package com.auhpp.event_management.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {
    @NotEmpty(message = "New password cannot empty")
    private String newPassword;

    @Email(message = "Email is invalid")
    @NotEmpty(message = "Email cannot empty")
    private String email;

    @NotEmpty(message = "Otp code cannot be empty")
    private String otpCode;
}

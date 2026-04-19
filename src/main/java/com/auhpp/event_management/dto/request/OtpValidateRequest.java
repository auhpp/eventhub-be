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
public class OtpValidateRequest {
    @NotEmpty(message = "Otp code cannot be empty")
    private String otpCode;

    @NotEmpty(message = "Email cannot be empty")
    private String email;
}

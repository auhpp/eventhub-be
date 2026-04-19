package com.auhpp.event_management.controller;

import com.auhpp.event_management.dto.request.OtpValidateRequest;
import com.auhpp.event_management.service.OtpService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/otp")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OtpController {
    OtpService otpService;

    @PostMapping("/verify")
    public ResponseEntity<Boolean> verify(
            @RequestBody OtpValidateRequest request
    ) {
        String otp = otpService.getOtp(request.getEmail());
        boolean res = otp.equals(request.getOtpCode());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }

}

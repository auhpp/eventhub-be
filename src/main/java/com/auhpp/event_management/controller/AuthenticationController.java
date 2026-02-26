package com.auhpp.event_management.controller;

import com.auhpp.event_management.dto.request.AuthenticationRequest;
import com.auhpp.event_management.dto.request.RegisterRequest;
import com.auhpp.event_management.dto.request.VerifyAndRegisterRequest;
import com.auhpp.event_management.dto.response.AuthenticationResponse;
import com.auhpp.event_management.dto.response.UserResponse;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.Duration;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    @NonFinal
    @Value("${spring.jwt.refreshable-duration}")
    long REFRESHABLE_DURATION;

    AuthenticationService authenticationService;


    @PostMapping("/register")
    public void sendOtpCreateUser(@Valid @RequestBody RegisterRequest registerRequest) {
        authenticationService.sendRegistrationOtp(registerRequest);
    }

    @PostMapping("/register/verify-otp")
    public void verifyAndCreateUser(@Valid @RequestBody VerifyAndRegisterRequest verifyAndRegisterRequest) {
        authenticationService.verifyAndCreateUser(verifyAndRegisterRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request) {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(request);
        ResponseCookie responseCookie = ResponseCookie.from("refresh_token",
                        authenticationResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofMinutes(REFRESHABLE_DURATION))
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(authenticationResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            @CookieValue(name = "refresh_token", defaultValue = "") String refreshToken
    ) throws ParseException, JOSEException {
        if (refreshToken.isEmpty()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        AuthenticationResponse result = authenticationService.refreshToken(refreshToken);
        ResponseCookie responseCookie = ResponseCookie.from("refresh_token",
                        result.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofMinutes(REFRESHABLE_DURATION))
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(result);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal Jwt jwt) {
        authenticationService.logout(jwt.getTokenValue());
        ResponseCookie deleteSpringCookie = ResponseCookie.from("refresh_token",
                        null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString())
                .build();
    }

    @GetMapping("/current-user")
    public ResponseEntity<UserResponse> getCurrentUserInfo() {
        UserResponse response = authenticationService.getCurrentUserInfo();
        return ResponseEntity
                .ok().body(response);
    }
}

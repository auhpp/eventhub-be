package com.auhpp.event_management.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    EMAIL_ALREADY_EXISTS(1000, "Email already exists", HttpStatus.BAD_REQUEST),
    OTP_NOT_VALID(1001, "Otp not valid", HttpStatus.BAD_REQUEST),
    OTP_NOT_FOUND(1002, "Otp not found", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1003, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(1004, "Forbidden", HttpStatus.FORBIDDEN),
    USER_NOT_FOUND(1005, "User not found", HttpStatus.NOT_FOUND),
    USER_LOCKED(1006, "User locked", HttpStatus.LOCKED),
    INVALID_TOKEN(1007, "Invalid token", HttpStatus.UNAUTHORIZED);
    private final int code;
    private final String message;
    private final HttpStatusCode status;

    ErrorCode(int code, String message, HttpStatusCode status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}

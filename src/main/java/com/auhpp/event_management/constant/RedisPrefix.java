package com.auhpp.event_management.constant;

import lombok.Getter;

@Getter
public enum RedisPrefix {
    OTP_KEY("otp:"), TOKEN_BLACKLIST("token_blacklist:"),
    BOOKING_EXPIRATION("booking_expiration:");

    private final String value;

    RedisPrefix(String value) {
        this.value = value;
    }
}

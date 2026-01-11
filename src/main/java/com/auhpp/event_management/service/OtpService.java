package com.auhpp.event_management.service;

public interface OtpService {
    String generateOTP();

    void saveOtp(String email, String otpCode);

    String getOtp(String email);

    void deleteOtp(String email);

    boolean verifyOtp(String email, String otpCode);
}

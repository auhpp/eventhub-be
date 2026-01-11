package com.auhpp.event_management.service;

import com.auhpp.event_management.constant.EmailType;

public interface EmailService {
    void sendOtpEmail(String email, EmailType type, String title);
}

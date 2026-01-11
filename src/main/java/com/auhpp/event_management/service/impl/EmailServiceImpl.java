package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.EmailType;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.service.EmailService;
import com.auhpp.event_management.service.OtpService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    JavaMailSender sender;
    OtpService otpService;
    SpringTemplateEngine templateEngine;

    @Async
    public void send(String to, String body, String subject) {
        try {
            MimeMessage mimeMessage = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(body, true);
            helper.setTo(to);
            helper.setSubject(subject);
            sender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendOtpEmail(String email, EmailType type, String title) {
        String otpCode = otpService.getOtp(email);
        if (otpCode == null) {
            throw new AppException(ErrorCode.OTP_NOT_FOUND);
        }
        String htmlBody = "";
        if (type.equals(EmailType.REGISTER)) {
            Context context = new Context();
            context.setVariable("actionMessage", "Xác nhận tạo tài khoản");
            context.setVariable("otpDigits", otpCode.split(""));

            htmlBody = templateEngine.process("otp-email", context);
        }
        send(email, htmlBody, title);
    }
}

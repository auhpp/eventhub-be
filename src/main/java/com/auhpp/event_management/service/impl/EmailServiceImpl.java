package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.EmailType;
import com.auhpp.event_management.constant.EventType;
import com.auhpp.event_management.constant.MeetingPlatform;
import com.auhpp.event_management.dto.request.StaffInvitationEmailRequest;
import com.auhpp.event_management.dto.request.TicketGiftEmailRequest;
import com.auhpp.event_management.entity.Event;
import com.auhpp.event_management.entity.EventSession;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.service.EmailService;
import com.auhpp.event_management.service.OtpService;
import com.auhpp.event_management.util.ValidateUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @NonFinal
    @Value("${app.fe.user-url}")
    String feUserUrl;

    JavaMailSender sender;
    OtpService otpService;
    SpringTemplateEngine templateEngine;

    @Async
    public void send(String to, String body, String subject) {
        try {
            if (!ValidateUtils.isValidEmail(to)) {
                throw new AppException(ErrorCode.INVALID_EMAIL);
            }
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

    private String formatEventDate(EventSession eventSession) {
        String eventDate = "";
        Locale vnLocale = new Locale("vi", "VN");
        DateTimeFormatter fullDateFormatter = DateTimeFormatter.ofPattern("EEEE, dd 'tháng' MM, yyyy", vnLocale);

        LocalDateTime eventStartDate = eventSession.getStartDateTime();
        LocalDateTime eventEndDate = eventSession.getEndDateTime();
        if (eventStartDate.equals(eventEndDate)) {
            eventDate = eventStartDate.format(fullDateFormatter);
        } else {
            eventDate = eventStartDate.format(fullDateFormatter) + " - " +
                    eventEndDate.format(fullDateFormatter);
        }
        return eventDate;
    }

    private String formatEventTime(EventSession eventSession) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalDateTime eventStartDate = eventSession.getStartDateTime();
        LocalDateTime eventEndDate = eventSession.getEndDateTime();

        return eventStartDate.format(timeFormatter) + " - " + eventEndDate.format(timeFormatter);
    }


    @Override
    public void sendEventInvitationEmail(String email, String token, Event event, EventSession eventSession, String message) {
        String eventDate = formatEventDate(eventSession);
        String eventTime = formatEventTime(eventSession);

        String location = event.getType() == EventType.OFFLINE ? event.getLocation() : (
                eventSession.getMeetingPlatform() == MeetingPlatform.GOOGLE_MEET ? "Google Meet" : "Zoom"
        );

        String viewLink = feUserUrl + "/invitation/response?token=" + token + "&eventId=" + event.getId();

        String htmlBody = "";
        Context context = new Context();
        context.setVariable("organizerAvatar", event.getAppUser().getAvatar());
        context.setVariable("organizerName", event.getAppUser().getFullName());
        context.setVariable("eventName", event.getName());
        context.setVariable("eventFullDate", eventDate);
        context.setVariable("eventTime", eventTime);
        context.setVariable("location", location);
        context.setVariable("viewLink", viewLink);
        context.setVariable("message", message);

        htmlBody = templateEngine.process("event-invitation-email", context);
        String title = "Bạn được mời tham gia " + event.getName();

        send(email, htmlBody, title);
    }

    @Override
    public void sendStaffInvitationEmail(StaffInvitationEmailRequest request) {
        Event event = request.getEvent();
        List<Map<String, String>> eventSessions = event.getEventSessions().stream().map(
                eventSession -> {
                    Map<String, String> sessionInfo = new HashMap<>();
                    sessionInfo.put("date", formatEventDate(eventSession));
                    sessionInfo.put("time", formatEventTime(eventSession));
                    return sessionInfo;
                }
        ).toList();
        String location = event.getType() == EventType.OFFLINE ? event.getLocation() : "Sự kiện online";

        String viewLink = feUserUrl + "/invitation/event-staff/response?token=" + request.getToken() + "&eventId=" + event.getId();
        String eventDetailLink = feUserUrl + "/event/" + event.getId();

        String htmlBody = "";
        Context context = new Context();
        context.setVariable("organizerAvatar", event.getAppUser().getAvatar());
        context.setVariable("organizerName", event.getAppUser().getFullName());
        context.setVariable("eventName", event.getName());
        context.setVariable("eventSessions", eventSessions);
        context.setVariable("location", location);
        context.setVariable("viewLink", viewLink);
        context.setVariable("eventDetailLink", eventDetailLink);
        context.setVariable("message", request.getMessage());
        context.setVariable("recipientEmail", request.getRecipientEmail());
        context.setVariable("roleName", request.getRoleName().getValue());

        htmlBody = templateEngine.process("staff-invitation-email", context);
        String title = "Bạn được mời tham gia đội ngũ tổ chức: " + event.getName();

        send(request.getRecipientEmail(), htmlBody, title);
    }

    @Override
    public void sendTicketGiftEmail(TicketGiftEmailRequest request) {
        String viewLink = feUserUrl + "/ticket-gift/" + request.getTicketGiftId();
        String eventDetailLink = feUserUrl + "/event/" + request.getEventId();

        String htmlBody = "";
        Context context = new Context();
        context.setVariable("senderAvatar", request.getSender().getAvatar());
        context.setVariable("senderName", request.getSender().getFullName());
        context.setVariable("eventName", request.getEventName());
        context.setVariable("ticketQuantity", request.getTicketQuantity());
        context.setVariable("claimLink", viewLink);
        context.setVariable("eventDetailLink", eventDetailLink);

        htmlBody = templateEngine.process("ticket-gift-email", context);
        String title = "Bạn có lời mời nhận vé - " + request.getEventName();
        send(request.getEmailReceiver(), htmlBody, title);
    }


}

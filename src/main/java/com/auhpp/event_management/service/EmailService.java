package com.auhpp.event_management.service;

import com.auhpp.event_management.constant.EmailType;
import com.auhpp.event_management.dto.request.StaffInvitationEmailRequest;
import com.auhpp.event_management.dto.request.TicketGiftEmailRequest;
import com.auhpp.event_management.entity.Event;
import com.auhpp.event_management.entity.EventSession;

public interface EmailService {
    void sendOtpEmail(String email, EmailType type, String title);

    void sendEventInvitationEmail(String email, String token, Event event,
                                  EventSession eventSession, String message);

    void sendStaffInvitationEmail(StaffInvitationEmailRequest request);

    void sendTicketGiftEmail(TicketGiftEmailRequest request);

}

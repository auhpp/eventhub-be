package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.TicketCreateRequest;
import com.auhpp.event_management.dto.response.TicketResponse;

public interface TicketService {
    TicketResponse createTicket(TicketCreateRequest ticketCreateRequest, Long eventSessionId);
}

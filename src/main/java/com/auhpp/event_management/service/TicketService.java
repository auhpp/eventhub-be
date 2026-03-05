package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.TicketCreateRequest;
import com.auhpp.event_management.dto.request.TicketUpdateRequest;
import com.auhpp.event_management.dto.response.TicketBasicResponse;
import com.auhpp.event_management.dto.response.TicketResponse;

import java.util.List;

public interface TicketService {
    TicketResponse createTicket(TicketCreateRequest ticketCreateRequest, Long eventSessionId);

    TicketResponse updateTicket(Long id, TicketUpdateRequest request);

    void deleteById(Long id);

    List<TicketBasicResponse> getTickets(Long eventSessionId);
}

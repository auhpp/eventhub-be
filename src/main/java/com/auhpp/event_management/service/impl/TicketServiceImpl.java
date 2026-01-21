package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.TicketStatus;
import com.auhpp.event_management.dto.request.TicketCreateRequest;
import com.auhpp.event_management.dto.response.TicketResponse;
import com.auhpp.event_management.entity.EventSession;
import com.auhpp.event_management.entity.Ticket;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.TicketMapper;
import com.auhpp.event_management.repository.EventSessionRepository;
import com.auhpp.event_management.repository.TicketRepository;
import com.auhpp.event_management.service.TicketService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    TicketRepository ticketRepository;
    EventSessionRepository eventSessionRepository;
    TicketMapper ticketMapper;

    @Override
    @Transactional
    public TicketResponse createTicket(TicketCreateRequest ticketCreateRequest,
                                       Long eventSessionId) {
        if (ticketCreateRequest.getOpenAt().isAfter(ticketCreateRequest.getEndAt())) {
            throw new AppException(ErrorCode.INVALID_PARAMS);
        }
        Ticket ticket = ticketMapper.toTicket(ticketCreateRequest);
        EventSession eventSession = eventSessionRepository.findById(eventSessionId).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        ticket.setEventSession(eventSession);
        ticket.setStatus(TicketStatus.PENDING);
        ticketRepository.save(ticket);
        return ticketMapper.toTicketResponse(ticket);
    }
}

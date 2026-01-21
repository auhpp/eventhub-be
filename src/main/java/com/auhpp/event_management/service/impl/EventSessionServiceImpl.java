package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.dto.request.EventSessionCreateRequest;
import com.auhpp.event_management.dto.request.TicketCreateRequest;
import com.auhpp.event_management.dto.response.EventSessionResponse;
import com.auhpp.event_management.entity.Event;
import com.auhpp.event_management.entity.EventSession;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.EventSessionMapper;
import com.auhpp.event_management.repository.EventRepository;
import com.auhpp.event_management.repository.EventSessionRepository;
import com.auhpp.event_management.service.EventSessionService;
import com.auhpp.event_management.service.TicketService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EventSessionServiceImpl implements EventSessionService {

    EventSessionRepository eventSessionRepository;
    EventSessionMapper eventSessionMapper;
    TicketService ticketService;
    EventRepository eventRepository;

    @Override
    @Transactional
    public EventSessionResponse createEventSession(EventSessionCreateRequest eventSessionCreateRequest,
                                                   Long eventId) {
        if (eventSessionCreateRequest.getStartDateTime().isAfter(eventSessionCreateRequest.getEndDateTime())) {
            throw new AppException(ErrorCode.INVALID_PARAMS);
        }
        EventSession eventSession = eventSessionMapper.toEventSession(eventSessionCreateRequest);
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        eventSession.setEvent(event);
        eventSessionRepository.save(eventSession);

        for (TicketCreateRequest ticketCreateRequest : eventSessionCreateRequest.getTicketCreateRequests()) {
            ticketService.createTicket(ticketCreateRequest, eventSession.getId());
        }
        return eventSessionMapper.toEventSessionResponse(eventSession);
    }
}

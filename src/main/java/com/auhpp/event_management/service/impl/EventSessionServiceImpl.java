package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.dto.request.EventSessionCreateRequest;
import com.auhpp.event_management.dto.request.EventSessionUpdateRequest;
import com.auhpp.event_management.dto.request.TicketCreateRequest;
import com.auhpp.event_management.dto.response.EventSessionResponse;
import com.auhpp.event_management.entity.Event;
import com.auhpp.event_management.entity.EventSession;
import com.auhpp.event_management.entity.Ticket;
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

    @Override
    public EventSessionResponse getEventSessionById(Long id) {
        EventSession eventSession = eventSessionRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        return eventSessionMapper.toEventSessionResponse(eventSession);
    }

    @Override
    @Transactional
    public EventSessionResponse updateEventSession(Long id, EventSessionUpdateRequest request) {
        EventSession eventSession = eventSessionRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (eventSession.isExpired()) {
            throw new AppException(ErrorCode.RESOURCE_CAN_NOT_UPDATE);
        }
        eventSessionMapper.updateEventSessionFromRequest(request, eventSession);
        eventSessionRepository.save(eventSession);
        return eventSessionMapper.toEventSessionResponse(eventSession);
    }

    @Override
    public void deleteById(Long id) {
        EventSession eventSession = eventSessionRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        Event event = eventSession.getEvent();
        boolean valid = true;
        for (Ticket ticket : eventSession.getTickets()) {
            if (!ticket.getAttendees().isEmpty()) {
                valid = false;
                break;
            }
        }
        if (valid && event.getEventSessions().size() > 1 && !eventSession.isExpired()) {
            eventSessionRepository.delete(eventSession);
        } else {
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND);
        }
    }


}

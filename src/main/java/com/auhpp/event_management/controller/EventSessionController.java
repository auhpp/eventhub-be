package com.auhpp.event_management.controller;

import com.auhpp.event_management.dto.request.EventSessionUpdateRequest;
import com.auhpp.event_management.dto.request.TicketCreateRequest;
import com.auhpp.event_management.dto.response.EventSessionResponse;
import com.auhpp.event_management.dto.response.TicketResponse;
import com.auhpp.event_management.service.EventSessionService;
import com.auhpp.event_management.service.TicketService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/event-session")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventSessionController {
    EventSessionService eventSessionService;
    TicketService ticketService;

    @GetMapping("/{eventSessionId}")
    public ResponseEntity<EventSessionResponse> getEventSessionById(
            @PathVariable(name = "eventSessionId") Long id
    ) {
        EventSessionResponse response = eventSessionService.getEventSessionById(id);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{eventSessionId}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<EventSessionResponse> updateEventSession(
            @PathVariable(name = "eventSessionId") Long id,
            @Valid @RequestBody EventSessionUpdateRequest request
    ) {
        EventSessionResponse response = eventSessionService.updateEventSession(id, request);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{eventSessionId}/ticket")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<TicketResponse> createTicket(
            @PathVariable(name = "eventSessionId") Long id,
            @Valid @RequestBody TicketCreateRequest request
    ) {
        TicketResponse response = ticketService.createTicket(request, id);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{eventSessionId}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<Void> deleteEventSession(
            @PathVariable(name = "eventSessionId") Long id
    ) {
        eventSessionService.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.OK).build();
    }

}

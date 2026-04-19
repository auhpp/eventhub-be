package com.auhpp.event_management.controller;

import com.auhpp.event_management.constant.TimeFilter;
import com.auhpp.event_management.dto.request.EventSessionUpdateRequest;
import com.auhpp.event_management.dto.request.TicketCreateRequest;
import com.auhpp.event_management.dto.response.*;
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
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<EventSessionResponse> updateEventSession(
            @PathVariable(name = "eventSessionId") Long id,
            @Valid @RequestBody EventSessionUpdateRequest request
    ) {
        EventSessionResponse response = eventSessionService.updateEventSession(id, request);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{eventSessionId}/ticket")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<TicketResponse> createTicket(
            @PathVariable(name = "eventSessionId") Long id,
            @Valid @RequestBody TicketCreateRequest request
    ) {
        TicketResponse response = ticketService.createTicket(request, id);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{eventSessionId}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<Void> deleteEventSession(
            @PathVariable(name = "eventSessionId") Long id
    ) {
        eventSessionService.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.OK).build();
    }


    @GetMapping("/report/check-in/{eventSessionId}")
    public ResponseEntity<EventSessionReportCheckInResponse> reportCheckin(
            @PathVariable(name = "eventSessionId") Long id
    ) {
        EventSessionReportCheckInResponse response = eventSessionService.reportCheckIn(id);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @PostMapping("/cancel/{eventSessionId}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<Void> cancelEventSession(
            @PathVariable(name = "eventSessionId") Long id
    ) {
        eventSessionService.cancelEventSession(id);
        return ResponseEntity
                .status(HttpStatus.OK).build();
    }



    @GetMapping("/{eventSessionId}/stats/chart")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<EventChartStatsResponse> getEventChartStats(
            @PathVariable(name = "eventSessionId") Long id,
            @RequestParam(name = "timeFilter") TimeFilter timeFilter
    ) {
        EventChartStatsResponse res = eventSessionService.getEventChartStats(id, timeFilter);
        return ResponseEntity
                .status(HttpStatus.OK).body(res);
    }

    @PostMapping("/{eventSessionId}/release-fund")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> releaseFundManually(
            @PathVariable(name = "eventSessionId") Long id
    ) {
        eventSessionService.releaseFundForEventSession(id);
        return ResponseEntity
                .status(HttpStatus.OK).build();
    }

}

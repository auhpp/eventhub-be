package com.auhpp.event_management.controller;

import com.auhpp.event_management.dto.request.TicketUpdateRequest;
import com.auhpp.event_management.dto.response.TicketResponse;
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
@RequestMapping("/api/v1/ticket")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketController {
    TicketService ticketService;

    @PutMapping("/{ticketId}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<TicketResponse> updateTicket(
            @PathVariable(name = "ticketId") Long id,
            @Valid @RequestBody TicketUpdateRequest request
    ) {
        TicketResponse response = ticketService.updateTicket(id, request);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{ticketId}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<Void> deleteTicket(
            @PathVariable(name = "ticketId") Long id
    ) {
        ticketService.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.OK).build();
    }

}

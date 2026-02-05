package com.auhpp.event_management.controller;

import com.auhpp.event_management.dto.request.EventInvitationRejectRequest;
import com.auhpp.event_management.dto.request.TicketGiftCreateRequest;
import com.auhpp.event_management.dto.request.TicketGiftSearchRequest;
import com.auhpp.event_management.dto.response.AttendeeBasicResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.TicketGiftResponse;
import com.auhpp.event_management.service.TicketGiftService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ticket-gift")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketGiftController {
    TicketGiftService ticketGiftService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<TicketGiftResponse> createTicketGift(
            @RequestBody @Valid TicketGiftCreateRequest request
    ) {
        TicketGiftResponse result = ticketGiftService.createTicketGift(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping("/accept/{id}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<TicketGiftResponse> acceptTicketGift(
            @PathVariable(name = "id") Long id
    ) {
        TicketGiftResponse result = ticketGiftService.accept(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping("/reject/{id}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<TicketGiftResponse> rejectTicketGift(
            @PathVariable(name = "id") Long id,
            @RequestBody EventInvitationRejectRequest request
    ) {
        TicketGiftResponse result = ticketGiftService.reject(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping("/filter")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<PageResponse<TicketGiftResponse>> getTicketGifts(
            @RequestBody TicketGiftSearchRequest request,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<TicketGiftResponse> result =
                ticketGiftService.getTicketGifts(request, page, size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<TicketGiftResponse> getTicketGiftById(
            @PathVariable(name = "id") Long id
    ) {
        TicketGiftResponse response = ticketGiftService.getById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/revoke/{id}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<TicketGiftResponse> revokeTicketGifts(
            @PathVariable("id") Long id
    ) {
        TicketGiftResponse response = ticketGiftService.revoke(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{id}/attendees")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<List<AttendeeBasicResponse>> getAttendees(
            @PathVariable("id") Long id
    ) {
        List<AttendeeBasicResponse> response = ticketGiftService.getAttendees(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}

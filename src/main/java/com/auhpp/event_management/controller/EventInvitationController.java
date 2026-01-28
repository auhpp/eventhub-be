package com.auhpp.event_management.controller;

import com.auhpp.event_management.dto.request.EventInvitationCreateRequest;
import com.auhpp.event_management.dto.request.EventInvitationRejectRequest;
import com.auhpp.event_management.dto.request.EventInvitationSearchRequest;
import com.auhpp.event_management.dto.response.EventInvitationResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.service.EventInvitationService;
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
@RequestMapping("/api/v1/event-invitation")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventInvitationController {
    EventInvitationService eventInvitationService;

    @PostMapping
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<List<EventInvitationResponse>> createEventInvitation(
            @Valid @RequestBody EventInvitationCreateRequest request
    ) {
        List<EventInvitationResponse> result = eventInvitationService.createEventInvitation(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping("/accept/{token}")
    public ResponseEntity<EventInvitationResponse> acceptEventInvitation(
            @PathVariable(name = "token") String token
    ) {
        EventInvitationResponse result = eventInvitationService.acceptEventInvitation(token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping("/reject/{token}")
    public ResponseEntity<EventInvitationResponse> rejectEventInvitation(
            @PathVariable(name = "token") String token,
            @RequestBody EventInvitationRejectRequest request
    ) {
        EventInvitationResponse result = eventInvitationService.rejectEventInvitation(token, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping("/filter")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<PageResponse<EventInvitationResponse>> getEventInvitations(
            @RequestBody EventInvitationSearchRequest request,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<EventInvitationResponse> result =
                eventInvitationService.getEventInvitations(request, page, size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }


    @GetMapping("/{token}")
    public ResponseEntity<EventInvitationResponse> getEventInvitationByToken(
            @PathVariable(name = "token") String token
    ) {
        EventInvitationResponse response = eventInvitationService.getByToken(token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/revoke/{id}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<Void> revokeEventInvitations(
            @PathVariable("id") Long id
    ) {
        eventInvitationService.revokeEventInvitation(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

}

package com.auhpp.event_management.controller;

import com.auhpp.event_management.dto.request.EventInvitationRejectRequest;
import com.auhpp.event_management.dto.request.EventStaffCreateRequest;
import com.auhpp.event_management.dto.request.EventStaffSearchRequest;
import com.auhpp.event_management.dto.response.EventStaffInvitationResponse;
import com.auhpp.event_management.dto.response.EventStaffResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.service.EventStaffService;
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
@RequestMapping("/api/v1/event-staff")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventStaffController {
    EventStaffService eventStaffService;


    @PostMapping
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<List<EventStaffInvitationResponse>> createEventStaff(
            @RequestBody @Valid EventStaffCreateRequest request
    ) {
        List<EventStaffInvitationResponse> result = eventStaffService.createEventStaff(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping("/accept/{token}")
    public ResponseEntity<EventStaffResponse> acceptEventStaff(
            @PathVariable(name = "token") String token
    ) {
        EventStaffResponse result = eventStaffService.acceptInvitation(token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping("/reject/{token}")
    public ResponseEntity<EventStaffResponse> rejectEventStaff(
            @PathVariable(name = "token") String token,
            @RequestBody EventInvitationRejectRequest request
    ) {
        EventStaffResponse result = eventStaffService.rejectInvitation(token, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping("/filter")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<PageResponse<EventStaffResponse>> getEventStaffs(
            @RequestBody EventStaffSearchRequest request,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<EventStaffResponse> result =
                eventStaffService.getEventStaffs(request, page, size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }


    @GetMapping("/{token}")
    public ResponseEntity<EventStaffResponse> getEventStaffByToken(
            @PathVariable(name = "token") String token
    ) {
        EventStaffResponse response = eventStaffService.getByToken(token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/filter/{id}")
    public ResponseEntity<EventStaffResponse> getEventStaffById(
            @PathVariable(name = "id") Long id
    ) {
        EventStaffResponse response = eventStaffService.getById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/revoke/{id}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<Void> revokeEventStaffs(
            @PathVariable("id") Long id
    ) {
        eventStaffService.revokeInvitation(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

}

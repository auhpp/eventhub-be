package com.auhpp.event_management.controller;

import com.auhpp.event_management.dto.request.AttendeeSearchRequest;
import com.auhpp.event_management.dto.request.CheckinSearchRequest;
import com.auhpp.event_management.dto.response.AttendeeResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.service.AttendeeService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/attendee")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttendeeController {
    AttendeeService attendeeService;

    @PostMapping("/current-user")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<PageResponse<AttendeeResponse>> getAttendeeByCurrentUser(
            @Valid @RequestBody AttendeeSearchRequest attendeeSearchRequest,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<AttendeeResponse> response = attendeeService.getAttendeesByCurrentUser(
                attendeeSearchRequest, page, size);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{attendeeId}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<AttendeeResponse> getAttendeeById(
            @PathVariable(name = "attendeeId") Long id
    ) {
        AttendeeResponse response = attendeeService.getAttendeeById(id);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{attendeeId}/assign/{email}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<AttendeeResponse> assignAttendeeEmail(
            @PathVariable(name = "attendeeId") Long id,
            @PathVariable(name = "email") String email
    ) {
        AttendeeResponse response = attendeeService.assignAttendeeEmail(id, email);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{attendeeId}/join-link")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<String> getMeetingUrl(
            @PathVariable(name = "attendeeId") Long id
    ) {
        String response = attendeeService.getMeetingLink(id);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @PostMapping("/check-in/filter")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<PageResponse<AttendeeResponse>> getAttendees(
            @Valid @RequestBody CheckinSearchRequest request,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<AttendeeResponse> response = attendeeService.getAttendees(request, page, size);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }
}

package com.auhpp.event_management.controller;

import com.auhpp.event_management.dto.request.OrganizerCreateRequest;
import com.auhpp.event_management.dto.request.OrganizerUpdateRequest;
import com.auhpp.event_management.dto.request.RejectionRequest;
import com.auhpp.event_management.dto.response.OrganizerRegistrationResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.service.OrganizerRegistrationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/organizer-registration")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrganizerRegistrationController {

    OrganizerRegistrationService organizerRegistrationService;

    // When uploading a file, you MUST NOT use @RequestBody (because it's for JSON).
    // You must use @ModelAttribute to map the fields in the form (including the file) to the DTO.
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrganizerRegistrationResponse> createOrganizerRegistrationRequest(
            @Valid @ModelAttribute OrganizerCreateRequest organizerCreateRequest
    ) {
        OrganizerRegistrationResponse result = organizerRegistrationService.createOrganizerRegistration(
                organizerCreateRequest
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping("/{registrationId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrganizerRegistrationResponse> updateOrganizerRegistrationRequest(
            @PathVariable("registrationId") Long id,
            @ModelAttribute OrganizerUpdateRequest organizerUpdateRequest
    ) {
        OrganizerRegistrationResponse result = organizerRegistrationService.updateOrganizerRegistration(
                id,
                organizerUpdateRequest
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @DeleteMapping("/{registrationId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteOrganizerRegistrationRequest(
            @PathVariable("registrationId") Long id
    ) {
        organizerRegistrationService.deleteOrganizerRegistration(
                id
        );
        return ResponseEntity
                .status(HttpStatus.OK).build();
    }

    @PostMapping("/cancel/{registrationId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> cancelOrganizerRegistrationRequest(
            @PathVariable("registrationId") Long id
    ) {
        organizerRegistrationService.cancelOrganizerRegistration(
                id
        );
        return ResponseEntity
                .status(HttpStatus.OK).build();
    }

    @PostMapping("/reject/{registrationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> rejectOrganizerRegistrationRequest(
            @PathVariable("registrationId") Long id, @RequestBody RejectionRequest request
    ) {
        organizerRegistrationService.rejectOrganizerRegistration(
                id, request
        );
        return ResponseEntity
                .status(HttpStatus.OK).build();
    }

    @PostMapping("/approve/{registrationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> approveOrganizerRegistrationRequest(
            @PathVariable("registrationId") Long id
    ) {
        organizerRegistrationService.approveOrganizerRegistration(
                id
        );
        return ResponseEntity
                .status(HttpStatus.OK).build();
    }

    @GetMapping("/current-user")
    @PreAuthorize("hasAnyRole('USER', 'ORGANIZER')")
    public ResponseEntity<PageResponse<OrganizerRegistrationResponse>> getOrganizerRegistrationsByUser(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<OrganizerRegistrationResponse> response =
                organizerRegistrationService.getOrganizerRegistrationsByUser(
                        page, size
                );
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<OrganizerRegistrationResponse>> getOrganizerRegistrations(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<OrganizerRegistrationResponse> response =
                organizerRegistrationService.getOrganizerRegistrations(
                        page, size
                );
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{registrationId}")
    public ResponseEntity<OrganizerRegistrationResponse> getOrganizerRegistrationById(
            @PathVariable(name = "registrationId") Long id
    ) {
        OrganizerRegistrationResponse response =
                organizerRegistrationService.getOrganizerRegistrationById(
                        id
                );
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

}

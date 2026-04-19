package com.auhpp.event_management.controller;

import com.auhpp.event_management.dto.request.AddCalendarRequest;
import com.auhpp.event_management.service.GoogleCalendarService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/api/v1/google-calendar")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GoogleCalendarController {
    GoogleCalendarService googleCalendarService;

    @PostMapping
    public ResponseEntity<Void> create(
            @RequestBody AddCalendarRequest request
    ) throws GeneralSecurityException, IOException {
        googleCalendarService.connectAndAddEvent(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}

package com.auhpp.event_management.controller;

import com.auhpp.event_management.dto.response.EventSessionResponse;
import com.auhpp.event_management.service.EventSessionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/event-session")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventSessionController {
    EventSessionService eventSessionService;

    @GetMapping("/{eventSessionId}")
    public ResponseEntity<EventSessionResponse> getEventSessionById(
            @PathVariable(name = "eventSessionId") Long id
    ) {
        EventSessionResponse response = eventSessionService.getEventSessionById(id);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }
}

package com.auhpp.event_management.controller;

import com.auhpp.event_management.dto.request.UserNoteCreateRequest;
import com.auhpp.event_management.dto.request.UserNoteSearchRequest;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.UserNoteResponse;
import com.auhpp.event_management.service.UserNoteService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user-note")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserNoteController {
    UserNoteService userNoteService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserNoteResponse> create(
            @Valid @ModelAttribute UserNoteCreateRequest request
    ) {
        UserNoteResponse result = userNoteService.create(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id") Long id) {
        userNoteService.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }


    @PostMapping("/filter")
    public ResponseEntity<PageResponse<UserNoteResponse>> filter(
            @RequestBody UserNoteSearchRequest request,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<UserNoteResponse> result = userNoteService.filter(request, page, size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }
}

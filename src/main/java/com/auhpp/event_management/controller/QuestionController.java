package com.auhpp.event_management.controller;

import com.auhpp.event_management.dto.request.QuestionCreateRequest;
import com.auhpp.event_management.dto.request.QuestionSearchRequest;
import com.auhpp.event_management.dto.request.QuestionUpdateRequest;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.QuestionResponse;
import com.auhpp.event_management.service.QuestionService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/question")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuestionController {
    QuestionService questionService;

    @PostMapping
    public ResponseEntity<QuestionResponse> create(
            @Valid @RequestBody QuestionCreateRequest request
    ) {
        QuestionResponse res = questionService.create(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionResponse> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody QuestionUpdateRequest request
    ) {
        QuestionResponse res = questionService.update(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id") Long id) {
        questionService.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/upvote/{id}")
    public ResponseEntity<Void> upvote(
            @PathVariable("id") Long id) {
        questionService.upvote(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/filter")
    public ResponseEntity<PageResponse<QuestionResponse>> filter(
            @RequestBody QuestionSearchRequest request,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<QuestionResponse> result = questionService.filter(request, page, size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }
}

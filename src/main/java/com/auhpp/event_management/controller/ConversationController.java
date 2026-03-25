package com.auhpp.event_management.controller;

import com.auhpp.event_management.dto.request.ConversationSearchRequest;
import com.auhpp.event_management.dto.request.ConversationUpdateRequest;
import com.auhpp.event_management.dto.response.ConversationResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.service.ConversationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/conversation")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationController {
    ConversationService conversationService;

    @PostMapping("/filter")
    public ResponseEntity<PageResponse<ConversationResponse>> getConversations(
            @RequestBody ConversationSearchRequest request,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<ConversationResponse> result = conversationService.getConversations(request, page, size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConversationResponse> findById(
            @PathVariable("id") Long id
    ) {
        ConversationResponse res = conversationService.findById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConversationResponse> update(
            @PathVariable("id") Long id,
            @RequestBody ConversationUpdateRequest request
    ) {
        ConversationResponse res = conversationService.update(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id") Long id
    ) {
        conversationService.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping("/other-member/{otherMemberId}")
    public ResponseEntity<ConversationResponse> findByOtherMember(
            @PathVariable("otherMemberId") Long otherMemberId
    ) {
        ConversationResponse res = conversationService.findByOtherMember(otherMemberId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }
}

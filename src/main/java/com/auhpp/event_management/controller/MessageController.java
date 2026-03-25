package com.auhpp.event_management.controller;

import com.auhpp.event_management.dto.request.CountUnseenMessageRequest;
import com.auhpp.event_management.dto.request.MessageCreateRequest;
import com.auhpp.event_management.dto.request.MessageSearchRequest;
import com.auhpp.event_management.dto.response.MessageResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.service.MessageService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/message")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageController {
    MessageService messageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResponse> create(
            @Valid @ModelAttribute MessageCreateRequest request
    ) {
        MessageResponse res = messageService.create(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }

    @PostMapping("/seen/{id}")
    public ResponseEntity<Void> seen(
            @PathVariable("id") Long id
    ) {
        messageService.seen(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/receive/{id}")
    public ResponseEntity<Void> receive(
            @PathVariable("id") Long id
    ) {
        messageService.receive(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/unseen")
    public ResponseEntity<Long> countUnSeenMessage(
            @RequestBody CountUnseenMessageRequest request
    ) {
        Long res = messageService.countUnSeenMessage(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }


    @PostMapping("/filter")
    public ResponseEntity<PageResponse<MessageResponse>> getMessages(
            @RequestBody MessageSearchRequest request,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<MessageResponse> result = messageService.getMessages(request, page, size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping("/seen/conversation/{conversationId}")
    public ResponseEntity<Void> seenByConversation(
            @PathVariable("conversationId") Long conversationId
    ) {
        messageService.seenByConversation(conversationId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/receive/all")
    public ResponseEntity<Void> receiveAll() {
        messageService.receiveAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}

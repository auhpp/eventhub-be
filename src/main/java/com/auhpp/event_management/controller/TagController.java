package com.auhpp.event_management.controller;

import com.auhpp.event_management.dto.request.TagCreateRequest;
import com.auhpp.event_management.dto.request.TagSearchRequest;
import com.auhpp.event_management.dto.request.TagUpdateRequest;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.TagResponse;
import com.auhpp.event_management.service.TagService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tag")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TagController {
    TagService tagService;

    @PostMapping
    public ResponseEntity<TagResponse> create(
            @Valid @RequestBody TagCreateRequest request
    ) {
        TagResponse res = tagService.create(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagResponse> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody TagUpdateRequest request
    ) {
        TagResponse res = tagService.update(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id") Long id
    ) {
        tagService.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/all")
    public ResponseEntity<List<TagResponse>> getAll(
            @RequestBody TagSearchRequest request
    ) {
        List<TagResponse> responses = tagService.getAll(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responses);
    }

    @PostMapping("/filter")
    public ResponseEntity<PageResponse<TagResponse>> filter(
            @RequestBody TagSearchRequest request,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<TagResponse> result = tagService.filter(request, page, size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }


}

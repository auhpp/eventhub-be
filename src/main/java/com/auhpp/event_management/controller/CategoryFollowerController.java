package com.auhpp.event_management.controller;

import com.auhpp.event_management.dto.request.CategoryFollowerCreateRequest;
import com.auhpp.event_management.dto.request.CategoryFollowerSearchRequest;
import com.auhpp.event_management.dto.response.CategoryFollowerResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.service.CategoryFollowerService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category-follower")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryFollowerController {
    CategoryFollowerService categoryFollowerService;

    @PostMapping
    public ResponseEntity<CategoryFollowerResponse> createCategoryFollower(
            @Valid @RequestBody CategoryFollowerCreateRequest request
    ) {
        CategoryFollowerResponse result = categoryFollowerService.createCategoryFollower(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryFollower(
            @PathVariable("id") Long id
    ) {
        categoryFollowerService.deleteCategoryFollower(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/filter")
    public ResponseEntity<PageResponse<CategoryFollowerResponse>> getCategoryFollowers(
            @RequestBody CategoryFollowerSearchRequest request,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<CategoryFollowerResponse> result = categoryFollowerService.getCategoryFollowers(
                request, page, size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping("/count")
    public ResponseEntity<Integer> countCategoryFollower(
            @RequestBody CategoryFollowerSearchRequest request
    ) {
        Integer result = categoryFollowerService.countCategoryFollower(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }
}

package com.auhpp.event_management.controller;


import com.auhpp.event_management.dto.request.CategoryCreateRequest;
import com.auhpp.event_management.dto.request.CategoryUpdateRequest;
import com.auhpp.event_management.dto.response.CategoryResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {

    CategoryService categoryService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @ModelAttribute CategoryCreateRequest categoryCreateRequest
    ) {
        CategoryResponse result = categoryService.createCategory(categoryCreateRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryResponse>> getCategories(
    ) {
        List<CategoryResponse> result = categoryService.getCategories();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @GetMapping
    public ResponseEntity<PageResponse<CategoryResponse>> getCategoriesPagination(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<CategoryResponse> result = categoryService.getCategoriesPagination(page, size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PutMapping(value = "/{categoryId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable("categoryId") Long id,
            @Valid @ModelAttribute CategoryUpdateRequest request
    ) {
        CategoryResponse result = categoryService.updateCategory(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @DeleteMapping(value = "/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable("categoryId") Long id
    ) {
        categoryService.deleteCategory(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}

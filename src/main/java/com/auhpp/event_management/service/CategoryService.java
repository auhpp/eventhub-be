package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.CategoryCreateRequest;
import com.auhpp.event_management.dto.request.CategoryUpdateRequest;
import com.auhpp.event_management.dto.response.CategoryResponse;
import com.auhpp.event_management.dto.response.PageResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryCreateRequest categoryCreateRequest);

    List<CategoryResponse> getCategories();

    PageResponse<CategoryResponse> getCategoriesPagination(int page, int size);

    CategoryResponse updateCategory(Long id, CategoryUpdateRequest request);

    void deleteCategory(Long id);
}

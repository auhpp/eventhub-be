package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.CategoryFollowerCreateRequest;
import com.auhpp.event_management.dto.request.CategoryFollowerSearchRequest;
import com.auhpp.event_management.dto.response.CategoryFollowerResponse;
import com.auhpp.event_management.dto.response.PageResponse;

public interface CategoryFollowerService {
    CategoryFollowerResponse createCategoryFollower(CategoryFollowerCreateRequest request);

    void deleteCategoryFollower(Long id);

    PageResponse<CategoryFollowerResponse> getCategoryFollowers(CategoryFollowerSearchRequest request,
                                                                int page, int size);

    Integer countCategoryFollower(CategoryFollowerSearchRequest request);
}

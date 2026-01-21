package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.request.CategoryCreateRequest;
import com.auhpp.event_management.dto.response.CategoryResponse;
import com.auhpp.event_management.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toCategoryResponse(Category category);

    Category toCategory(CategoryCreateRequest categoryCreateRequest);

}

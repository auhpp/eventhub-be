package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.FolderName;
import com.auhpp.event_management.constant.SortBy;
import com.auhpp.event_management.dto.request.CategoryCreateRequest;
import com.auhpp.event_management.dto.request.CategorySearchRequest;
import com.auhpp.event_management.dto.request.CategoryUpdateRequest;
import com.auhpp.event_management.dto.response.CategoryResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.entity.Category;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.CategoryMapper;
import com.auhpp.event_management.repository.CategoryRepository;
import com.auhpp.event_management.service.CategoryService;
import com.auhpp.event_management.service.CloudinaryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import java.util.List;
import java.util.Map;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;
    CloudinaryService cloudinaryService;

    private void uploadCategoryAvatar(Category category, MultipartFile file) {
        Map<String, Object> uploadResult = cloudinaryService.uploadFile(file,
                FolderName.CATEGORY.getValue(), true);
        String publicId = (String) uploadResult.get("public_id");
        String imageUrl = (String) uploadResult.get("secure_url");
        category.setAvatarUrl(imageUrl);
        category.setAvatarPublicId(publicId);
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryCreateRequest categoryCreateRequest) {
        Category category = categoryMapper.toCategory(categoryCreateRequest);
        uploadCategoryAvatar(category, categoryCreateRequest.getAvatar());
        categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    public List<CategoryResponse> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(
                categoryMapper::toCategoryResponse
        ).toList();
    }

    @Override
    public PageResponse<CategoryResponse> getCategoriesPagination(CategorySearchRequest request, int page, int size) {
        Sort.Direction direction = Sort.Direction.DESC;
        if (request.getSortOrder() != null && request.getSortOrder() == SortOrder.ASCENDING) {
            direction = Sort.Direction.ASC;
        }
        String paramSort = "followerCount";
        if (request.getSortBy() != null && request.getSortBy() == SortBy.EVENT) {
            paramSort = "eventCount";
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(direction,
                paramSort));
        Page<Object[]> pageData = categoryRepository.filter(request.getName(), pageable);
        List<CategoryResponse> responses = pageData.getContent().stream().map(
                objects -> {
                    CategoryResponse res = categoryMapper.toCategoryResponse((Category) objects[0]);
                    res.setFollowerCount((Long) objects[1]);
                    res.setEventCount((Long) objects[2]);
                    return res;
                }
        ).toList();
        return PageResponse.<CategoryResponse>builder()
                .currentPage(page)
                .totalElements(pageData.getTotalElements())
                .totalPage(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .data(responses)
                .build();
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryUpdateRequest request) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        categoryMapper.updateCategoryFromRequest(request, category);
        if (request.getAvatar() != null) {
            // delete image
            cloudinaryService.deleteFile(category.getAvatarPublicId());

            // upload image
            uploadCategoryAvatar(category, request.getAvatar());
        }
        categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (category.getEvents().isEmpty()) {
            categoryRepository.delete(category);
        } else {
            throw new AppException(ErrorCode.RESOURCE_CAN_NOT_DELETE);
        }
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        return categoryMapper.toCategoryResponse(category);
    }
}

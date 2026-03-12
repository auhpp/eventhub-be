package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.dto.request.CategoryFollowerCreateRequest;
import com.auhpp.event_management.dto.request.CategoryFollowerSearchRequest;
import com.auhpp.event_management.dto.response.CategoryFollowerResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.entity.AppUser;
import com.auhpp.event_management.entity.Category;
import com.auhpp.event_management.entity.CategoryFollower;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.CategoryFollowerMapper;
import com.auhpp.event_management.repository.AppUserRepository;
import com.auhpp.event_management.repository.CategoryFollowerRepository;
import com.auhpp.event_management.repository.CategoryRepository;
import com.auhpp.event_management.service.CategoryFollowerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CategoryFollowerServiceImpl implements CategoryFollowerService {

    AppUserRepository appUserRepository;
    CategoryFollowerRepository categoryFollowerRepository;
    CategoryFollowerMapper followerMapper;
    CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryFollowerResponse createCategoryFollower(CategoryFollowerCreateRequest request) {
        if (categoryFollowerRepository.findByAppUserIdAndCategoryId(request.getUserId(),
                request.getCategoryId()).isPresent()) {
            throw new AppException(ErrorCode.INVALID_PARAMS);
        }
        AppUser appUser = appUserRepository.findById(request.getUserId()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        CategoryFollower categoryFollower = CategoryFollower.builder()
                .appUser(appUser)
                .category(category)
                .build();
        categoryFollowerRepository.save(categoryFollower);
        return followerMapper.toCategoryFollowerResponse(categoryFollower);
    }

    @Override
    @Transactional
    public void deleteCategoryFollower(Long id) {
        categoryFollowerRepository.deleteById(id);
    }

    @Override
    public PageResponse<CategoryFollowerResponse> getCategoryFollowers(
            CategoryFollowerSearchRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC,
                "createdAt"));
        Page<CategoryFollower> pageData = categoryFollowerRepository.filterCategoryFollower(
                request.getUserId(), request.getCategoryId(), pageable
        );
        List<CategoryFollowerResponse> responses = pageData.getContent().stream().map(
                followerMapper::toCategoryFollowerResponse
        ).toList();
        return PageResponse.<CategoryFollowerResponse>builder()
                .currentPage(page)
                .totalElements(pageData.getTotalElements())
                .totalPage(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .data(responses)
                .build();
    }

    @Override
    public Integer countCategoryFollower(CategoryFollowerSearchRequest request) {
        return categoryFollowerRepository.countCategoryFollower(request.getUserId(), request.getCategoryId());
    }
}

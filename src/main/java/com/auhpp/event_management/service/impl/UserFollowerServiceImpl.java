package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.dto.request.UserFollowerCreateRequest;
import com.auhpp.event_management.dto.request.UserFollowerSearchRequest;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.UserFollowerResponse;
import com.auhpp.event_management.entity.AppUser;
import com.auhpp.event_management.entity.UserFollower;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.UserFollowerMapper;
import com.auhpp.event_management.repository.AppUserRepository;
import com.auhpp.event_management.repository.UserFollowerRepository;
import com.auhpp.event_management.service.UserFollowerService;
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
public class UserFollowerServiceImpl implements UserFollowerService {

    AppUserRepository appUserRepository;
    UserFollowerRepository userFollowerRepository;
    UserFollowerMapper userFollowerMapper;

    @Override
    @Transactional
    public UserFollowerResponse createUserFollower(UserFollowerCreateRequest request) {
        if (userFollowerRepository.findByFollowerIdAndFollowedId(request.getFollowerId(),
                request.getFollowedId()).isPresent()) {
            throw new AppException(ErrorCode.INVALID_PARAMS);
        }
        AppUser follower = appUserRepository.findById(request.getFollowerId()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        AppUser followed = appUserRepository.findById(request.getFollowedId()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        UserFollower categoryFollower = UserFollower.builder()
                .follower(follower)
                .followed(followed)
                .build();
        userFollowerRepository.save(categoryFollower);
        return userFollowerMapper.toUserFollowerResponse(categoryFollower);
    }

    @Override
    @Transactional
    public void deleteUserFollower(Long id) {
        userFollowerRepository.deleteById(id);
    }

    @Override
    public PageResponse<UserFollowerResponse> getUserFollowers(
            UserFollowerSearchRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC,
                "createdAt"));
        Page<UserFollower> pageData = userFollowerRepository.filterUserFollower(
                request.getFollowerId(), request.getFollowedId(), pageable
        );
        List<UserFollowerResponse> responses = pageData.getContent().stream().map(
                userFollowerMapper::toUserFollowerResponse
        ).toList();
        return PageResponse.<UserFollowerResponse>builder()
                .currentPage(page)
                .totalElements(pageData.getTotalElements())
                .totalPage(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .data(responses)
                .build();
    }

    @Override
    public Integer countUserFollowers(UserFollowerSearchRequest request) {
        return userFollowerRepository.countUserFollower(request.getFollowerId(), request.getFollowedId());
    }
}

package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.UserFollowerCreateRequest;
import com.auhpp.event_management.dto.request.UserFollowerSearchRequest;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.UserFollowerResponse;

public interface UserFollowerService {
    UserFollowerResponse createUserFollower(UserFollowerCreateRequest request);

    void deleteUserFollower(Long id);

    PageResponse<UserFollowerResponse> getUserFollowers(UserFollowerSearchRequest request,
                                                        int page, int size);

    Integer countUserFollowers(UserFollowerSearchRequest request);

}

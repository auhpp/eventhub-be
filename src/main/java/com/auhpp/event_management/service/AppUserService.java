package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.PasswordChangeRequest;
import com.auhpp.event_management.dto.request.SocialLinkCreateRequest;
import com.auhpp.event_management.dto.request.UserUpdateRequest;
import com.auhpp.event_management.dto.response.SocialLinkResponse;
import com.auhpp.event_management.dto.response.UserBasicResponse;

import java.util.List;

public interface AppUserService {
    UserBasicResponse updateInfoUser(Long id, UserUpdateRequest request);

    void changePassword(Long id, PasswordChangeRequest request);

    List<SocialLinkResponse> createSocialLink(List<SocialLinkCreateRequest> requests);
}

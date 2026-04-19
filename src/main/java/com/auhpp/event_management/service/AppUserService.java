package com.auhpp.event_management.service;

import com.alibaba.excel.ExcelWriter;
import com.auhpp.event_management.dto.request.*;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.SocialLinkResponse;
import com.auhpp.event_management.dto.response.UserBasicResponse;
import com.auhpp.event_management.dto.response.UserResponse;

import java.util.List;

public interface AppUserService {
    UserBasicResponse updateInfoUser(Long id, UserUpdateRequest request);

    void changePassword(Long id, PasswordChangeRequest request);

    List<SocialLinkResponse> createSocialLink(List<SocialLinkCreateRequest> requests);

    UserBasicResponse getByEmail(String email);

    UserResponse getById(Long id);

    void changeRole(Long id, UserChangeRoleRequest request);

    void sendEmailCreateAdminUser(AdminUserCreateRequest request);

    void confirmAdminUserAccount(RegisterRequest request);

    PageResponse<UserBasicResponse> filter(UserSearchRequest request, int page, int size);

    void changeStatus(Long id, UserChangeStatusRequest request);

    void updateSocialLink(List<SocialLinkCreateRequest> requests);

    void exportReportAppUser(ExcelWriter excelWriter, UserSearchRequest request);

    void sendOtpResetPassword(EmailSendRequest request);

    void resetPassword(ResetPasswordRequest request);

}

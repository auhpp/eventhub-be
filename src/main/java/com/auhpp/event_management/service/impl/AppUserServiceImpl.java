package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.FolderName;
import com.auhpp.event_management.constant.RoleName;
import com.auhpp.event_management.dto.request.*;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.SocialLinkResponse;
import com.auhpp.event_management.dto.response.UserBasicResponse;
import com.auhpp.event_management.dto.response.UserResponse;
import com.auhpp.event_management.entity.*;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.SocialLinkMapper;
import com.auhpp.event_management.mapper.UserBasicMapper;
import com.auhpp.event_management.mapper.UserMapper;
import com.auhpp.event_management.repository.AppUserRepository;
import com.auhpp.event_management.repository.RoleRepository;
import com.auhpp.event_management.repository.SocialLinkRepository;
import com.auhpp.event_management.repository.TagRepository;
import com.auhpp.event_management.service.AppUserService;
import com.auhpp.event_management.service.AuthenticationService;
import com.auhpp.event_management.service.CloudinaryService;
import com.auhpp.event_management.service.EmailService;
import com.auhpp.event_management.util.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    AppUserRepository appUserRepository;
    UserBasicMapper userBasicMapper;
    UserMapper userMapper;
    CloudinaryService cloudinaryService;
    PasswordEncoder passwordEncoder;
    SocialLinkMapper socialLinkMapper;
    SocialLinkRepository socialLinkRepository;
    TagRepository tagRepository;
    RoleRepository roleRepository;
    AuthenticationService authenticationService;
    EmailService emailService;

    @Override
    @Transactional
    public UserBasicResponse updateInfoUser(Long id, UserUpdateRequest request) {
        AppUser user = appUserRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        userMapper.updateUserFromRequest(request, user);
        if (request.getAvatar() != null) {
            if (!StringUtils.hasText(user.getAvatar())) {
                cloudinaryService.deleteFile(user.getAvatarPublicId());
            }
            Map<String, Object> uploadResult = cloudinaryService.uploadFile(request.getAvatar(),
                    FolderName.USER_AVATAR.getValue() + user.getEmail(), true);
            String publicId = (String) uploadResult.get("public_id");
            String imageUrl = (String) uploadResult.get("secure_url");
            user.setAvatar(imageUrl);
            user.setAvatarPublicId(publicId);
        }

        // handle tag
        if (request.getTagIds() != null) {
            if (user.getUserTags() != null) {
                user.getUserTags().clear();
            } else {
                user.setUserTags(new ArrayList<>());
            }
            List<UserTag> userTags = user.getUserTags();
            for (Long tagId : request.getTagIds()) {
                Tag tag = tagRepository.findById(tagId).orElseThrow(
                        () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
                );
                userTags.add(UserTag.builder()
                        .appUser(user)
                        .tag(tag)
                        .build());
            }
            user.setUserTags(userTags);
        }

        appUserRepository.save(user);
        return userBasicMapper.toUserBasicResponse(user);
    }

    @Override
    @Transactional
    public void changePassword(Long id, PasswordChangeRequest request) {
        AppUser user = appUserRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            appUserRepository.save(user);
        } else {
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        }
    }

    @Override
    @Transactional
    public List<SocialLinkResponse> createSocialLink(List<SocialLinkCreateRequest> requests) {
        String email = SecurityUtils.getCurrentUserLogin();
        AppUser user = appUserRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        List<SocialLinkResponse> responses = new ArrayList<>();
        for (SocialLinkCreateRequest request : requests) {
            Optional<SocialLink> optionalSocialLink = socialLinkRepository.findByTypeAndAppUserId(
                    request.getType(), user.getId());
            SocialLink socialLink;
            if (optionalSocialLink.isPresent()) {
                socialLink = optionalSocialLink.get();
                socialLinkMapper.updateSocialLinkFromRequest(request, socialLink);
            } else {
                socialLink = socialLinkMapper.toSocialLink(request);
            }
            socialLinkRepository.save(socialLink);
            responses.add(socialLinkMapper.toSocialLinkResponse(socialLink));
        }
        return responses;
    }

    @Override
    public UserBasicResponse getByEmail(String email) {
        Optional<AppUser> user = appUserRepository.findByEmail(email);
        return user.map(userBasicMapper::toUserBasicResponse).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
    }

    @Override
    public UserResponse getById(Long id) {
        return appUserRepository.findById(id).map(userMapper::toUserResponse).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
    }

    @Override
    @Transactional
    public void changeRole(Long id, UserChangeRoleRequest request) {
        AppUser user = appUserRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        Role role = roleRepository.findByName(request.getRoleName());
        user.setRole(role);
        appUserRepository.save(user);
    }

    @Override
    @Transactional
    public void sendEmailCreateAdminUser(AdminUserCreateRequest request) {
        Optional<AppUser> user = appUserRepository.findByEmail(request.getEmail());
        Role role = roleRepository.findByName(RoleName.ADMIN);

        if (!request.isResend()) {
            if (user.isPresent()) {
                throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
            }
            if (request.getExpireDateTime().isBefore(LocalDateTime.now())) {
                throw new AppException(ErrorCode.INVALID_PARAMS);
            }
            AppUser inactiveUser = AppUser.builder()
                    .email(request.getEmail())
                    .role(role)
                    .status(false)
                    .build();
            appUserRepository.save(inactiveUser);
        }
        // send email
        String accessToken = authenticationService.generateToken(AppUser.builder()
                        .email(request.getEmail())
                        .role(role)
                        .build(),
                ChronoUnit.MINUTES.between(LocalDateTime.now(), request.getExpireDateTime()),
                true);
        emailService.sendEmailCreateAdminUser(request.getEmail(), accessToken);

    }

    @Override
    @Transactional
    public void confirmAdminUserAccount(RegisterRequest request) {
        String email = SecurityUtils.getCurrentUserLogin();
        AppUser user = appUserRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (user.getStatus()) {
            throw new AppException(ErrorCode.ACCOUNT_ACTIVE);

        }
        if (!Objects.equals(email, request.getEmail())) {
            throw new AppException(ErrorCode.INVALID_PARAMS);
        }

        String passwordEncoded = passwordEncoder.encode(request.getPassword());
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoded);
        user.setStatus(true);

        appUserRepository.save(user);
    }

    @Override
    public PageResponse<UserBasicResponse> filter(UserSearchRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC,
                "createdAt"));
        Page<AppUser> pageData = appUserRepository.filter(request.getEmail(), request.getStatus(),
                request.getRoleName(), pageable);
        List<UserBasicResponse> responses = pageData.getContent().stream().map(
                userBasicMapper::toUserBasicResponse
        ).toList();
        return PageResponse.<UserBasicResponse>builder()
                .currentPage(page)
                .totalElements(pageData.getTotalElements())
                .totalPage(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .data(responses)
                .build();
    }

    @Override
    @Transactional
    public void changeStatus(Long id, UserChangeStatusRequest request) {
        AppUser user = appUserRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        user.setStatus(request.getStatus());
        appUserRepository.save(user);
    }


}

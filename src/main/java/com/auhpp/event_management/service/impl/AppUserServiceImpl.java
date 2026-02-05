package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.FolderName;
import com.auhpp.event_management.dto.request.PasswordChangeRequest;
import com.auhpp.event_management.dto.request.SocialLinkCreateRequest;
import com.auhpp.event_management.dto.request.UserUpdateRequest;
import com.auhpp.event_management.dto.response.SocialLinkResponse;
import com.auhpp.event_management.dto.response.UserBasicResponse;
import com.auhpp.event_management.entity.AppUser;
import com.auhpp.event_management.entity.SocialLink;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.SocialLinkMapper;
import com.auhpp.event_management.mapper.UserBasicMapper;
import com.auhpp.event_management.mapper.UserMapper;
import com.auhpp.event_management.repository.AppUserRepository;
import com.auhpp.event_management.repository.SocialLinkRepository;
import com.auhpp.event_management.service.AppUserService;
import com.auhpp.event_management.service.CloudinaryService;
import com.auhpp.event_management.util.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
                    FolderName.USER_AVATAR.getValue() + user.getEmail());
            String publicId = (String) uploadResult.get("public_id");
            String imageUrl = (String) uploadResult.get("secure_url");
            user.setAvatar(imageUrl);
            user.setAvatarPublicId(publicId);
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
        return user.map(userBasicMapper::toUserBasicResponse).orElse(null);

    }

}

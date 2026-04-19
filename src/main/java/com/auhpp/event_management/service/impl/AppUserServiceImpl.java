package com.auhpp.event_management.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.auhpp.event_management.constant.EmailType;
import com.auhpp.event_management.constant.FolderName;
import com.auhpp.event_management.constant.RoleName;
import com.auhpp.event_management.dto.request.*;
import com.auhpp.event_management.dto.response.*;
import com.auhpp.event_management.entity.AppUser;
import com.auhpp.event_management.entity.Role;
import com.auhpp.event_management.entity.SocialLink;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.SocialLinkMapper;
import com.auhpp.event_management.mapper.UserBasicMapper;
import com.auhpp.event_management.mapper.UserExportMapper;
import com.auhpp.event_management.mapper.UserMapper;
import com.auhpp.event_management.repository.AppUserRepository;
import com.auhpp.event_management.repository.RoleRepository;
import com.auhpp.event_management.repository.SocialLinkRepository;
import com.auhpp.event_management.service.*;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

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
    RoleRepository roleRepository;
    AuthenticationService authenticationService;
    EmailService emailService;
    UserExportMapper userExportMapper;
    OtpService otpService;

    @Override
    @Transactional
    public UserBasicResponse updateInfoUser(Long id, UserUpdateRequest request) {
        AppUser user = appUserRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        userMapper.updateUserFromRequest(request, user);
        if (request.getAvatar() != null) {
            if (user.getAvatar() != null) {
                cloudinaryService.deleteFile(user.getAvatarPublicId());
            }
            Map<String, Object> uploadResult = cloudinaryService.uploadFile(request.getAvatar(),
                    FolderName.USER_AVATAR.getValue() + user.getEmail(), true);
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
            if (request.getUrlLink() == "" || request.getUrlLink() == null) {
                throw new AppException(ErrorCode.INVALID_PARAMS);
            }
            Optional<SocialLink> optionalSocialLink = socialLinkRepository.findByTypeAndAppUserId(
                    request.getType(), user.getId());
            SocialLink socialLink;
            if (optionalSocialLink.isPresent()) {
                socialLink = optionalSocialLink.get();
                socialLinkMapper.updateSocialLinkFromRequest(request, socialLink);
            } else {
                socialLink = socialLinkMapper.toSocialLink(request);
            }
            socialLink.setAppUser(user);
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

    @Override
    @Transactional
    public void updateSocialLink(List<SocialLinkCreateRequest> requests) {
        for (SocialLinkCreateRequest request : requests) {
            SocialLink link = socialLinkRepository.findById(request.getId()).orElseThrow(
                    () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
            );
            link.setUrlLink(request.getUrlLink());
            socialLinkRepository.save(link);
        }
    }

    @Override
    public void exportReportAppUser(ExcelWriter excelWriter, UserSearchRequest request) {
        WriteSheet writeSheet = EasyExcel.writerSheet("danh_sach_tai_khoan")
                .relativeHeadRowIndex(1)
                .registerWriteHandler(new EventTitleWriteHandler("Danh sách tài khoản"))
                .build();
        int currentPage = 1;
        int pageSize = 1000;
        boolean hasNextPage = true;
        boolean hasData = false;

        while (hasNextPage) {
            Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.ASC,
                    "createdAt"));
            Page<AppUser> pageData = appUserRepository.filter(request.getEmail(), request.getStatus(),
                    request.getRoleName(), pageable);
            if (pageData == null || pageData.isEmpty()) {
                break;
            }
            hasData = true;
            List<UserExcelReportResponse> userExports = pageData.stream().map(
                    appUser -> {
                        UserExcelReportResponse response = userExportMapper.toUserExcelReportResponse(appUser);
                        response.setRoleName(appUser.getRole().getName().name());
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
                        response.setCreatedAt(appUser.getCreatedAt().format(formatter));
                        if (appUser.getLastSeen() != null) {
                            response.setLastSeen(appUser.getLastSeen().format(formatter));
                        }

                        return response;
                    }
            ).collect(Collectors.toList());

            excelWriter.write(userExports, writeSheet);

            if (currentPage >= pageData.getTotalPages()) {
                hasNextPage = false;
            } else {
                currentPage++;
            }
        }
        if (!hasData) {
            excelWriter.write(new ArrayList<UserExcelReportResponse>(), writeSheet);
        }
        excelWriter.finish();
    }

    @Override
    public void sendOtpResetPassword(EmailSendRequest request) {
        Optional<AppUser> appUserOptional = appUserRepository.findByEmail(request.getEmail());
        if (appUserOptional.isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        String otpCode = otpService.generateOTP();
        otpService.saveOtp(request.getEmail(), otpCode);
        // send otp
        emailService.sendOtpEmail(request.getEmail(), EmailType.FORGET_PASSWORD, "Khôi phục mật khẩu");
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        if (otpService.verifyOtp(request.getEmail(), request.getOtpCode())) {
            AppUser user = appUserRepository.findByEmail(request.getEmail()).orElseThrow(
                    () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
            );
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            appUserRepository.save(user);
        } else {
            throw new AppException(ErrorCode.OTP_NOT_VALID);
        }
    }


}

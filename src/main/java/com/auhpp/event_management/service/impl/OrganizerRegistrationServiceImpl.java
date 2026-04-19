package com.auhpp.event_management.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.auhpp.event_management.constant.FolderName;
import com.auhpp.event_management.constant.RegistrationStatus;
import com.auhpp.event_management.constant.RoleName;
import com.auhpp.event_management.constant.WalletType;
import com.auhpp.event_management.dto.request.*;
import com.auhpp.event_management.dto.response.BookingReportResponse;
import com.auhpp.event_management.dto.response.OrganizerRegistrationExcelReportResponse;
import com.auhpp.event_management.dto.response.OrganizerRegistrationResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.entity.AppUser;
import com.auhpp.event_management.entity.OrganizerRegistration;
import com.auhpp.event_management.entity.Role;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.OrganizerExportMapper;
import com.auhpp.event_management.mapper.OrganizerRegistrationMapper;
import com.auhpp.event_management.repository.AppUserRepository;
import com.auhpp.event_management.repository.OrganizerRegistrationRepository;
import com.auhpp.event_management.repository.RoleRepository;
import com.auhpp.event_management.service.CloudinaryService;
import com.auhpp.event_management.service.OrganizerRegistrationService;
import com.auhpp.event_management.service.WalletService;
import com.auhpp.event_management.util.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class OrganizerRegistrationServiceImpl implements OrganizerRegistrationService {
    OrganizerRegistrationRepository organizerRegistrationRepository;
    OrganizerRegistrationMapper organizerRegistrationMapper;
    AppUserRepository appUserRepository;
    RoleRepository roleRepository;
    CloudinaryService cloudinaryService;
    WalletService walletService;
    OrganizerExportMapper organizerExportMapper;

    @Override
    @Transactional
    public OrganizerRegistrationResponse createOrganizerRegistration(OrganizerCreateRequest organizerCreateRequest) {
        String email = SecurityUtils.getCurrentUserLogin();
        AppUser user = appUserRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
        if (user.getRole().getName() == RoleName.ORGANIZER) {
            throw new AppException(ErrorCode.ORGANIZER_ALREADY);
        }
        OrganizerRegistration organizerRegistration = organizerRegistrationMapper
                .toOrganizerRegistration(organizerCreateRequest);
        organizerRegistration.setStatus(RegistrationStatus.PENDING);


        organizerRegistration.setAppUser(user);

        Map<String, Object> uploadResult = cloudinaryService.uploadFile(organizerCreateRequest.getBusinessAvatar(),
                FolderName.ORGANIZER_REGISTRATION.getValue() + user.getEmail(), true);
        String publicId = (String) uploadResult.get("public_id");
        String imageUrl = (String) uploadResult.get("secure_url");
        organizerRegistration.setBusinessAvatarUrl(imageUrl);
        organizerRegistration.setAvatarPublicId(publicId);


        organizerRegistrationRepository.save(organizerRegistration);
        return organizerRegistrationMapper.toOrganizerRegistrationResponse(organizerRegistration);
    }

    @Override
    @Transactional
    public OrganizerRegistrationResponse updateOrganizerRegistration(
            long id,
            OrganizerUpdateRequest organizerUpdateRequest
    ) {
        OrganizerRegistration organizerRegistration = organizerRegistrationRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        SecurityUtils.isOwner(organizerRegistration.getAppUser());
        organizerRegistrationMapper.updateOrganizerRegistrationFromRequest(
                organizerUpdateRequest, organizerRegistration
        );
        if (organizerUpdateRequest.getBusinessAvatar() != null) {
            cloudinaryService.deleteFile(organizerRegistration.getAvatarPublicId());

            Map<String, Object> uploadResult = cloudinaryService.uploadFile(organizerUpdateRequest.getBusinessAvatar(),
                    FolderName.ORGANIZER_REGISTRATION.getValue() + organizerRegistration.getAppUser().getEmail(),
                    true);
            String publicId = (String) uploadResult.get("public_id");
            String imageUrl = (String) uploadResult.get("secure_url");
            organizerRegistration.setBusinessAvatarUrl(imageUrl);
            organizerRegistration.setAvatarPublicId(publicId);
        }

        organizerRegistrationRepository.save(organizerRegistration);
        return organizerRegistrationMapper.toOrganizerRegistrationResponse(
                organizerRegistration
        );
    }

    @Override
    @Transactional
    public void deleteOrganizerRegistration(long id) {
        OrganizerRegistration organizerRegistration = organizerRegistrationRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        SecurityUtils.isOwner(organizerRegistration.getAppUser());
        if (organizerRegistration.getStatus() == RegistrationStatus.PENDING) {
            cloudinaryService.deleteFile(organizerRegistration.getAvatarPublicId());
            organizerRegistrationRepository.delete(organizerRegistration);
        } else {
            throw new AppException(ErrorCode.RESOURCE_CAN_NOT_DELETE);
        }
    }

    @Override
    @Transactional
    public void cancelOrganizerRegistration(long id) {
        OrganizerRegistration organizerRegistration = organizerRegistrationRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        SecurityUtils.isOwner(organizerRegistration.getAppUser());
        if (organizerRegistration.getStatus() == RegistrationStatus.PENDING) {
            organizerRegistration.setStatus(RegistrationStatus.CANCELLED);
            organizerRegistrationRepository.save(organizerRegistration);
        } else {
            throw new AppException(ErrorCode.RESOURCE_CAN_NOT_CANCEL);
        }
    }

    @Override
    @Transactional
    public void rejectOrganizerRegistration(long id, RejectionRequest rejectionRequest) {
        OrganizerRegistration organizerRegistration = organizerRegistrationRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (organizerRegistration.getStatus() == RegistrationStatus.PENDING) {
            organizerRegistration.setStatus(RegistrationStatus.REJECTED);
            organizerRegistration.setRejectionReason(rejectionRequest.getReason());
            organizerRegistrationRepository.save(organizerRegistration);
        } else {
            throw new AppException(ErrorCode.RESOURCE_CAN_NOT_UPDATE);
        }
    }

    @Override
    @Transactional
    public void approveOrganizerRegistration(long id) {
        OrganizerRegistration organizerRegistration = organizerRegistrationRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (organizerRegistration.getStatus() == RegistrationStatus.PENDING) {

            organizerRegistration.setStatus(RegistrationStatus.APPROVED);
            organizerRegistrationRepository.save(organizerRegistration);

            Role role = roleRepository.findByName(RoleName.ORGANIZER);
            AppUser register = organizerRegistration.getAppUser();
            register.setRole(role);
            register.setAvatar(organizerRegistration.getBusinessAvatarUrl());
            register.setAvatarPublicId(organizerRegistration.getAvatarPublicId());
            register.setFullName(organizerRegistration.getBusinessName());
            appUserRepository.save(register);

            // wallet
            walletService.create(
                    WalletCreateRequest.builder()
                            .type(WalletType.ORGANIZER_WALLET)
                            .appUserId(register.getId())
                            .build()
            );

        } else {
            throw new AppException(ErrorCode.RESOURCE_CAN_NOT_UPDATE);
        }
    }

    @Override
    public PageResponse<OrganizerRegistrationResponse> getOrganizerRegistrations(
            OrganizerRegistrationSearchRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC,
                "createdAt"));
        Page<OrganizerRegistration> pageData = organizerRegistrationRepository.filter(request.getUserId(),
                request.getEmail(), request.getOrganizerName(), request.getStatus(), request.getFromDate(),
                request.getToDate(), pageable);
        List<OrganizerRegistrationResponse> responseData = pageData.getContent().stream().map(
                organizerRegistrationMapper::toOrganizerRegistrationResponse
        ).toList();
        return PageResponse.<OrganizerRegistrationResponse>builder()
                .currentPage(page)
                .totalElements(pageData.getTotalElements())
                .totalPage(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .data(responseData)
                .build();
    }

    @Override
    public OrganizerRegistrationResponse getOrganizerRegistrationById(Long id) {
        OrganizerRegistration organizerRegistration = organizerRegistrationRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        return organizerRegistrationMapper.toOrganizerRegistrationResponse(organizerRegistration);
    }

    @Override
    public Integer count(RegistrationStatus status) {
        return organizerRegistrationRepository.countByStatus(status);
    }

    @Override
    public void exportReportOrganizer(ExcelWriter excelWriter, OrganizerRegistrationSearchRequest request) {

        WriteSheet writeSheet = EasyExcel.writerSheet("danh_sach_ho_so_nha_to_chuc")
                .relativeHeadRowIndex(1)
                .registerWriteHandler(new EventTitleWriteHandler("Danh sách hồ sơ nhà tổ chức"))
                .build();
        int currentPage = 1;
        int pageSize = 1000;
        boolean hasNextPage = true;
        boolean hasData = false;

        while (hasNextPage) {
            Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.ASC,
                    "createdAt"));
            Page<OrganizerRegistration> pageData = organizerRegistrationRepository.filter(request.getUserId(),
                    request.getEmail(), request.getOrganizerName(), request.getStatus(), request.getFromDate(),
                    request.getToDate(), pageable);
            if (pageData == null || pageData.isEmpty()) {
                break;
            }
            hasData = true;
            List<OrganizerRegistrationExcelReportResponse> organizerRegistrationReports = pageData.stream().map(
                    organizerRegistration -> {
                        OrganizerRegistrationExcelReportResponse organizerRegistrationReport = organizerExportMapper.toOrganizerRegistrationExcelReportResponse(
                                organizerRegistration
                        );

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
                        organizerRegistrationReport.setCreatedAt(organizerRegistration.getCreatedAt().format(formatter));
                        organizerRegistrationReport.setUpdatedAt(organizerRegistration.getUpdatedAt().format(formatter));

                        organizerRegistrationReport.setType(organizerRegistration.getType().name());
                        organizerRegistrationReport.setStatus(organizerRegistration.getStatus().name());

                        return organizerRegistrationReport;
                    }
            ).collect(Collectors.toList());

            excelWriter.write(organizerRegistrationReports, writeSheet);

            if (currentPage >= pageData.getTotalPages()) {
                hasNextPage = false;
            } else {
                currentPage++;
            }
        }
        if (!hasData) {
            excelWriter.write(new ArrayList<OrganizerRegistrationExcelReportResponse>(), writeSheet);
        }
        excelWriter.finish();

    }
}

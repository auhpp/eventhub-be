package com.auhpp.event_management.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.auhpp.event_management.constant.*;
import com.auhpp.event_management.dto.request.WalletTransactionCreateRequest;
import com.auhpp.event_management.dto.request.WithdrawalRequestCreateRequest;
import com.auhpp.event_management.dto.request.WithdrawalRequestSearchRequest;
import com.auhpp.event_management.dto.request.WithdrawalRequestUpdateRequest;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.WithdrawalRequestExcelReportResponse;
import com.auhpp.event_management.dto.response.WithdrawalRequestResponse;
import com.auhpp.event_management.entity.AppUser;
import com.auhpp.event_management.entity.Wallet;
import com.auhpp.event_management.entity.WithdrawalRequest;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.WithdrawalRequestExportMapper;
import com.auhpp.event_management.mapper.WithdrawalRequestMapper;
import com.auhpp.event_management.repository.AppUserRepository;
import com.auhpp.event_management.repository.WalletRepository;
import com.auhpp.event_management.repository.WithdrawalRequestRepository;
import com.auhpp.event_management.service.CloudinaryService;
import com.auhpp.event_management.service.WalletTransactionService;
import com.auhpp.event_management.service.WithdrawalRequestService;
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
public class WithdrawalRequestServiceImpl implements WithdrawalRequestService {
    WithdrawalRequestRepository withdrawalRequestRepository;
    WithdrawalRequestMapper withdrawalRequestMapper;
    WalletRepository walletRepository;
    CloudinaryService cloudinaryService;
    AppUserRepository appUserRepository;
    WalletTransactionService walletTransactionService;
    WithdrawalRequestExportMapper withdrawalRequestExportMapper;

    @Override
    @Transactional
    public WithdrawalRequestResponse create(WithdrawalRequestCreateRequest request) {
        WithdrawalRequest withdrawalRequest = withdrawalRequestMapper.toWithdrawalRequest(request);
        Wallet wallet = walletRepository.findById(request.getWalletId()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        double availableBalance = wallet.getAvailableBalance();
        if (request.getAmount() > availableBalance) {
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND);
        }
        // update locked and available balance wallet
        double lockedBalance = wallet.getLockedBalance() == null ? 0 : wallet.getLockedBalance();
        wallet.setLockedBalance(request.getAmount() + lockedBalance);
        wallet.setAvailableBalance(wallet.getAvailableBalance() - request.getAmount());

        walletRepository.save(wallet);
        withdrawalRequest.setWallet(wallet);

        withdrawalRequest.setStatus(WithdrawalStatus.PENDING);
        withdrawalRequestRepository.save(withdrawalRequest);
        return withdrawalRequestMapper.toWithdrawalRequestResponse(withdrawalRequest);
    }

    @Override
    @Transactional
    public WithdrawalRequestResponse update(Long id, WithdrawalRequestUpdateRequest request) {
        WithdrawalRequest withdrawalRequest = withdrawalRequestRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        Wallet wallet = withdrawalRequest.getWallet();
        AppUser currentUser = appUserRepository.findByEmail(SecurityUtils.getCurrentUserLogin()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        RoleName currentUserRole = currentUser.getRole().getName();
        if (request.getStatus() != null) {
            withdrawalRequest.setStatus(request.getStatus());
        }
        if (currentUserRole == RoleName.USER || currentUserRole == RoleName.ORGANIZER) {
            if (withdrawalRequest.getStatus() == WithdrawalStatus.PENDING) {
                withdrawalRequestMapper.update(request, withdrawalRequest);
            }
        } else if (currentUserRole == RoleName.ADMIN) {
            withdrawalRequest.setAdminNote(request.getAdminNote());
            if (request.getStatus() != null && withdrawalRequest.getStatus() == WithdrawalStatus.COMPLETED) {
                if (request.getProofImage() != null) {
                    Map<String, Object> uploadResult = cloudinaryService.uploadFile(request.getProofImage(),
                            FolderName.WITHDRAWAL.getValue(), true);
                    String publicId = (String) uploadResult.get("public_id");
                    String imageUrl = (String) uploadResult.get("secure_url");
                    withdrawalRequest.setProofImageUrl(imageUrl);
                    withdrawalRequest.setProofImagePublicId(publicId);
                }
                // create wallet transaction
                walletTransactionService.create(WalletTransactionCreateRequest.builder()
                        .amount(withdrawalRequest.getAmount() * (-1))
                        .balanceBefore(wallet.getAvailableBalance() + withdrawalRequest.getAmount())
                        .balanceAfter(wallet.getAvailableBalance())
                        .transactionType(TransactionType.WITHDRAWAL)
                        .referenceId(id)
                        .referenceType(ReferenceType.WITHDRAWAL_REQUEST)
                        .description(WalletTransactionDescription.WITHDRAWAL_REQUEST.getValue() + "#" + id)
                        .walletId(wallet.getId())
                        .build());
                // update locked balance wallet
                wallet.setLockedBalance(wallet.getLockedBalance() - withdrawalRequest.getAmount());
                walletRepository.save(wallet);
            }
        }
        if (request.getStatus() != null && (request.getStatus() == WithdrawalStatus.CANCELLED ||
                request.getStatus() == WithdrawalStatus.REJECTED)) {
            // update locked and available balance wallet
            wallet.setLockedBalance(wallet.getLockedBalance() - withdrawalRequest.getAmount());
            wallet.setAvailableBalance(wallet.getAvailableBalance() + withdrawalRequest.getAmount());

            walletRepository.save(wallet);
        }
        withdrawalRequestRepository.save(withdrawalRequest);
        return withdrawalRequestMapper.toWithdrawalRequestResponse(withdrawalRequest);
    }

    @Override
    public PageResponse<WithdrawalRequestResponse> filter(WithdrawalRequestSearchRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC,
                "createdAt"));
        List<WithdrawalStatus> statuses = request.getStatus() == null ? null : List.of(request.getStatus());
        Page<WithdrawalRequest> pageData = withdrawalRequestRepository.filter(
                request.getUserEmail(), statuses, request.getFromDate(), request.getToDate(), pageable);
        List<WithdrawalRequestResponse> responses = pageData.getContent().stream().map(
                withdrawalRequestMapper::toWithdrawalRequestResponse
        ).toList();
        return PageResponse.<WithdrawalRequestResponse>builder()
                .currentPage(page)
                .totalElements(pageData.getTotalElements())
                .totalPage(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .data(responses)
                .build();
    }

    @Override
    public WithdrawalRequestResponse findById(Long id) {
        WithdrawalRequest withdrawalRequest = withdrawalRequestRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        return withdrawalRequestMapper.toWithdrawalRequestResponse(withdrawalRequest);
    }

    @Override
    public void exportReportWithdrawal(ExcelWriter excelWriter, WithdrawalRequestSearchRequest request) {
        WriteSheet writeSheet = EasyExcel.writerSheet("danh_sach_yeu_cau_rut_tien")
                .relativeHeadRowIndex(1)
                .registerWriteHandler(new EventTitleWriteHandler("Danh sách yêu cầu rút tiền"))
                .build();
        int currentPage = 1;
        int pageSize = 1000;
        boolean hasNextPage = true;
        boolean hasData = false;

        while (hasNextPage) {
            Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.ASC,
                    "createdAt"));
            List<WithdrawalStatus> statuses = request.getStatus() == null ? null : List.of(request.getStatus());
            Page<WithdrawalRequest> pageData = withdrawalRequestRepository.filter(
                    request.getUserEmail(), statuses, request.getFromDate(), request.getToDate(), pageable);
            if (pageData == null || pageData.isEmpty()) {
                break;
            }
            hasData = true;
            List<WithdrawalRequestExcelReportResponse> withdrawalRequestReports = pageData.stream().map(
                    withdrawalRequest -> {
                        WithdrawalRequestExcelReportResponse withdrawalRequestReport =
                                withdrawalRequestExportMapper.toWithdrawalRequestExcelReportResponse(withdrawalRequest);

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
                        withdrawalRequestReport.setCreatedAt(withdrawalRequest.getCreatedAt().format(formatter));
                        withdrawalRequestReport.setUpdatedAt(withdrawalRequest.getUpdatedAt().format(formatter));

                        withdrawalRequestReport.setWalletType(withdrawalRequest.getWallet().getType().name());
                        withdrawalRequestReport.setStatus(withdrawalRequest.getStatus().name());
                        return withdrawalRequestReport;
                    }
            ).collect(Collectors.toList());

            excelWriter.write(withdrawalRequestReports, writeSheet);

            if (currentPage >= pageData.getTotalPages()) {
                hasNextPage = false;
            } else {
                currentPage++;
            }
        }
        if (!hasData) {
            excelWriter.write(new ArrayList<WithdrawalRequestExcelReportResponse>(), writeSheet);
        }
        excelWriter.finish();

    }
}

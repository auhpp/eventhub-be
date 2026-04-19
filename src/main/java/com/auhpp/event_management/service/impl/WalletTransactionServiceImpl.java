package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.TransactionType;
import com.auhpp.event_management.dto.request.WalletTransactionCreateRequest;
import com.auhpp.event_management.dto.request.WalletTransactionSearchRequest;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.WalletTransactionResponse;
import com.auhpp.event_management.entity.Wallet;
import com.auhpp.event_management.entity.WalletTransaction;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.WalletTransactionMapper;
import com.auhpp.event_management.repository.WalletRepository;
import com.auhpp.event_management.repository.WalletTransactionRepository;
import com.auhpp.event_management.service.WalletTransactionService;
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
public class WalletTransactionServiceImpl implements WalletTransactionService {
    WalletTransactionRepository walletTransactionRepository;
    WalletRepository walletRepository;
    WalletTransactionMapper walletTransactionMapper;

    @Override
    @Transactional
    public WalletTransactionResponse create(WalletTransactionCreateRequest request) {
        if (request.getAmount() == 0) {
            throw new AppException(ErrorCode.INVALID_PARAMS);
        }
        if (request.getAmount() > 0 && request.getBalanceAfter() < request.getBalanceBefore()) {
            throw new AppException(ErrorCode.INVALID_PARAMS);
        }
        if (request.getAmount() < 0 && request.getBalanceAfter() > request.getBalanceBefore()) {
            throw new AppException(ErrorCode.INVALID_PARAMS);
        }
        WalletTransaction walletTransaction = walletTransactionMapper.toWalletTransaction(request);
        Wallet wallet = walletRepository.findById(request.getWalletId()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        walletTransaction.setWallet(wallet);
        walletTransactionRepository.save(walletTransaction);
        return walletTransactionMapper.toWalletTransactionResponse(walletTransaction);
    }

    @Override
    public PageResponse<WalletTransactionResponse> filter(WalletTransactionSearchRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC,
                "createdAt"));
        List<TransactionType> transactionTypes = request.getTransactionType() == null ? null : List.of(request.getTransactionType());
        Page<WalletTransaction> pageData = walletTransactionRepository.filter(request.getWalletId(),
                transactionTypes,
                pageable);
        List<WalletTransactionResponse> responses = pageData.getContent().stream().map(
                walletTransactionMapper::toWalletTransactionResponse
        ).toList();
        return PageResponse.<WalletTransactionResponse>builder()
                .currentPage(page)
                .totalElements(pageData.getTotalElements())
                .totalPage(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .data(responses)
                .build();
    }
}

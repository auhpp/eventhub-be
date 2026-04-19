package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.BookingType;
import com.auhpp.event_management.constant.ReferenceType;
import com.auhpp.event_management.constant.TransactionType;
import com.auhpp.event_management.constant.WalletType;
import com.auhpp.event_management.dto.request.WalletCreateRequest;
import com.auhpp.event_management.dto.request.WalletSearchRequest;
import com.auhpp.event_management.dto.request.WalletTransactionCreateRequest;
import com.auhpp.event_management.dto.request.WalletUpdateRequest;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.WalletResponse;
import com.auhpp.event_management.entity.*;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.WalletMapper;
import com.auhpp.event_management.repository.AppUserRepository;
import com.auhpp.event_management.repository.BookingRepository;
import com.auhpp.event_management.repository.WalletRepository;
import com.auhpp.event_management.repository.WalletTransactionRepository;
import com.auhpp.event_management.service.WalletService;
import com.auhpp.event_management.service.WalletTransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    WalletRepository walletRepository;
    WalletMapper walletMapper;
    AppUserRepository appUserRepository;
    BookingRepository bookingRepository;
    WalletTransactionService walletTransactionService;

    @Override
    @Transactional
    public WalletResponse create(WalletCreateRequest request) {
        if (request.getAppUserId() != null && request.getType() != null) {
            AppUser appUser = appUserRepository.findById(request.getAppUserId()).orElseThrow(
                    () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
            );
            List<Wallet> wallets = appUser.getWallets();
            if (wallets != null) {
                if (wallets.size() == 2) {
                    throw new AppException(ErrorCode.INVALID_PARAMS);
                }
                for (Wallet wallet : wallets) {
                    if (wallet.getType() == request.getType()) {
                        throw new AppException(ErrorCode.INVALID_PARAMS);
                    }
                }
            }
            Wallet wallet = Wallet.builder()
                    .type(request.getType())
                    .availableBalance(0D)
                    .pendingBalance(0D)
                    .lockedBalance(0D)
                    .appUser(appUser)
                    .build();
            walletRepository.save(wallet);
            return walletMapper.toWalletResponse(wallet);
        } else {
            throw new AppException(ErrorCode.INVALID_PARAMS);
        }
    }

    @Override
    @Transactional
    public WalletResponse update(Long id, WalletUpdateRequest request) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        walletMapper.update(request, wallet);
        walletRepository.save(wallet);
        return walletMapper.toWalletResponse(wallet);
    }

    @Override
    public PageResponse<WalletResponse> filter(WalletSearchRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC,
                "createdAt"));
        Page<Wallet> pageData = walletRepository.filter(request.getUserId(), request.getUserEmail(),
                request.getType(), request.getStatus(), pageable);
        List<WalletResponse> responses = pageData.getContent().stream().map(
                walletMapper::toWalletResponse
        ).toList();
        return PageResponse.<WalletResponse>builder()
                .currentPage(page)
                .totalElements(pageData.getTotalElements())
                .totalPage(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .data(responses)
                .build();
    }

    @Override
    public WalletResponse findById(Long id) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        return walletMapper.toWalletResponse(wallet);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processSingleBooking(Booking booking) {
        if (booking.getIsFundReleased()) {
            return;
        }
        // define user and wallet
        WalletType walletType = booking.getType() == BookingType.BUY ? WalletType.ORGANIZER_WALLET :
                WalletType.USER_WALLET;
        Event event = booking.getAttendees().getFirst().getTicket().getEventSession().getEvent();

        AppUser appUser = booking.getType() == BookingType.BUY ? event.getAppUser() :
                booking.getResalePost().getAppUser();
        Wallet wallet = appUser.getWallets().stream().filter(
                w -> w.getType() == walletType
        ).findFirst().orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        // calculate commission
        double commissionFee = 0;
        for (Attendee attendee : booking.getAttendees()) {
            if (booking.getType() == BookingType.BUY) {
                double commRate = event.getCommissionRate() != null ? event.getCommissionRate() : 0.0;
                double fixedFee = event.getCommissionFixedPerTicket() != null ? event.getCommissionFixedPerTicket() : 0.0;

                double basePriceForCommission = attendee.getFinalPrice() != null ? attendee.getFinalPrice() : attendee.getPrice();

                commissionFee += (basePriceForCommission * (commRate / 100) +
                        fixedFee);
            } else if (booking.getType() == BookingType.RESALE) {
                double commRate = booking.getResalePost().getCommissionRate() != null ?
                        booking.getResalePost().getCommissionRate() : 0.0;

                double basePriceForCommission = attendee.getFinalPrice() != null ? attendee.getFinalPrice() : attendee.getPrice();

                commissionFee += basePriceForCommission * (commRate / 100);
            }
        }

        double totalAmountPaid = booking.getFinalAmount();
        double actualRevenue = totalAmountPaid - commissionFee;

        // update wallet
        double currentPending = wallet.getPendingBalance() == null ? 0 : wallet.getPendingBalance();
        double currentAvailable = wallet.getAvailableBalance() == null ? 0 : wallet.getAvailableBalance();

        double newPending = currentPending - totalAmountPaid;
        wallet.setPendingBalance(newPending);
        wallet.setAvailableBalance(currentAvailable + actualRevenue);
        walletRepository.save(wallet);

        // create wallet transaction
        if (actualRevenue > 0) {
            walletTransactionService.create(WalletTransactionCreateRequest.builder()
                    .amount(actualRevenue)
                    .balanceBefore(currentAvailable)
                    .balanceAfter(currentAvailable + actualRevenue)
                    .transactionType(TransactionType.FUND_RELEASE)
                    .referenceId(booking.getId())
                    .referenceType(booking.getType() == BookingType.BUY ? ReferenceType.BOOKING :
                            ReferenceType.RESELL_BOOKING)
                    .description(String.format("Đối soát vé sự kiện. Tổng: %,.0f, Phí sàn: %,.0f", totalAmountPaid,
                            commissionFee)).walletId(wallet.getId())
                    .walletId(wallet.getId())
                    .build());
        }
        booking.setIsFundReleased(true);
        bookingRepository.save(booking);
    }
}

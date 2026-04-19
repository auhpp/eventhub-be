package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.request.WalletTransactionCreateRequest;
import com.auhpp.event_management.dto.response.WalletTransactionResponse;
import com.auhpp.event_management.entity.WalletTransaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletTransactionMapper {
    WalletTransactionResponse toWalletTransactionResponse(WalletTransaction walletTransaction);

    WalletTransaction toWalletTransaction(WalletTransactionCreateRequest request);
}

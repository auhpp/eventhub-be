package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransactionSearchRequest {
    private TransactionType transactionType;

    private Long walletId;

}

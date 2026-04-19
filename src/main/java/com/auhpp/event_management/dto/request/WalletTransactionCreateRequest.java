package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.ReferenceType;
import com.auhpp.event_management.constant.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransactionCreateRequest {

    private Double amount;

    private Double balanceBefore;

    private Double balanceAfter;

    private TransactionType transactionType;

    private ReferenceType referenceType;

    private Long referenceId;

    private String description;

    private Long walletId;
}

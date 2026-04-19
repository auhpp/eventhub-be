package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.ReferenceType;
import com.auhpp.event_management.constant.TransactionType;
import com.auhpp.event_management.entity.Wallet;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletTransactionResponse {
    private Long id;

    private Double amount;

    private Double balanceBefore;

    private Double balanceAfter;

    private TransactionType transactionType;

    private ReferenceType referenceType;

    private Long referenceId;

    private String description;

    private LocalDateTime createdAt;
}

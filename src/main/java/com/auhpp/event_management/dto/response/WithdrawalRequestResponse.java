package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.WithdrawalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawalRequestResponse {
    private Long id;

    private Double amount;

    private String bankCode;

    private String bankAccountNo;

    private String bankAccountName;

    private WithdrawalStatus status;

    private String proofImageUrl;

    private String proofImagePublicId;

    private String adminNote;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private WalletResponse wallet;
}

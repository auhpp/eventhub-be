package com.auhpp.event_management.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalRequestCreateRequest {
    @NotNull(message = "Amount cannot null")
    private Double amount;

    @NotEmpty(message = "Bank code cannot empty")
    private String bankCode;

    @NotEmpty(message = "bankAccountNo cannot empty")
    private String bankAccountNo;

    @NotEmpty(message = "bankAccountName cannot empty")
    private String bankAccountName;

    @NotNull(message = "walletId cannot null")
    private Long walletId;
}

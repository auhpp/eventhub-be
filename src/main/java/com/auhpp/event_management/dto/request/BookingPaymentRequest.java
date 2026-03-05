package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.WalletType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingPaymentRequest {
    private Double finalAmount;

    private Long couponId;

    @NotEmpty(message = "Customer name cannot be empty")
    private String customerName;

    @NotEmpty(message = "Customer phone cannot be empty")
    private String customerPhone;

    @NotNull(message = "Wallet type cannot be null")
    private WalletType walletType;

    private String note;

    private String transactionId;
}

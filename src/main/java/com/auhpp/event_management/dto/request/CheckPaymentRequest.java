package com.auhpp.event_management.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckPaymentRequest {
    @NotEmpty(message = "VnpTxnRef cannot be empty")
    private String vnpTxnRef;
}

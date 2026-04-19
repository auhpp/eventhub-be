package com.auhpp.event_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethodResponse {
    private Long id;

    private String bankCode;

    private String bankAccountNo;

    private String bankAccountName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

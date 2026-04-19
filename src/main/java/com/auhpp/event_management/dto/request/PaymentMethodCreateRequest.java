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
public class PaymentMethodCreateRequest {

    @NotEmpty(message = "Bank code cannot empty")
    private String bankCode;

    @NotEmpty(message = "Bank account no cannot empty")
    private String bankAccountNo;

    @NotEmpty(message = "Bank account name cannot empty")
    private String bankAccountName;
}

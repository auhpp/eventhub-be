package com.auhpp.event_management.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckPaymentRequest {
    @NotEmpty(message = "VnpTxnRef cannot be empty")
    private String vnpTxnRef;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddHHmmss")
    @NotNull(message = "vnpPayDate cannot be empty")
    private LocalDateTime vnpPayDate;
}

package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.WithdrawalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalRequestSearchRequest {
    private String userEmail;

    private WithdrawalStatus status;

    private LocalDateTime fromDate;
    private LocalDateTime toDate;
}

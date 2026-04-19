package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.WalletStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletUpdateRequest {
    private Double availableBalance;

    private Double pendingBalance;

    private Double lockedBalance;

    private WalletStatus status;

}

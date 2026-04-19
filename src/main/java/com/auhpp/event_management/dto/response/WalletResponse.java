package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.WalletStatus;
import com.auhpp.event_management.constant.WalletType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletResponse {
    private Long id;

    private WalletType type;

    private Double availableBalance;

    private Double pendingBalance;

    private Double lockedBalance;

    private WalletStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private UserBasicResponse appUser;
}

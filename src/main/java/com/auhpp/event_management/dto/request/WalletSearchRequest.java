package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.WalletStatus;
import com.auhpp.event_management.constant.WalletType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletSearchRequest {
    private String userEmail;

    private Long userId;

    private WalletStatus status;

    private WalletType type;
}

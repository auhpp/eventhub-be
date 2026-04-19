package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.WalletType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletCreateRequest {
    private WalletType type;

    private Long appUserId;
}

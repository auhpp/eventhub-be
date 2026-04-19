package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.EventSearchStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponSearchRequest {
    private Long eventId;
    private Boolean hasPublic;

    private String keyword;
    private EventSearchStatus status;
}

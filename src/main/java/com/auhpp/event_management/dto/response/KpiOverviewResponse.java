package com.auhpp.event_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KpiOverviewResponse {
    private Double totalFmv;  // tổng doanh thu

    private Double commissionFromEvents; // Hoa hồng từ sự kiện

    private Double commissionFomResales; // Hoa hồng từ bán lại vé

    private Integer activeEventsCount;

    private Integer pendingEventsCount;

    private Integer rejectEventsCount;

    private Integer cancelEventsCount;

    private Integer pendingResalesCount;

    private Integer newUsersCount;
}


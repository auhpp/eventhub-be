package com.auhpp.event_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopResaleEventResponse {
    private Long eventId;

    private String eventName;

    private Long resalePostCount;

    private Long completedTransactionCount;

    private Double totalFee;

}

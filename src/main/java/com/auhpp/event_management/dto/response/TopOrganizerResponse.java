package com.auhpp.event_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopOrganizerResponse {
    private Double totalCommissionGenerated;

    private Long organizerId;

    private Long eventCount;

    private String organizerName;



}

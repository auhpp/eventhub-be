package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.TimeFilter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventChartStatsResponse {
    private TimeFilter timeFilter;
    private List<EventChartDataPoint> data;
}

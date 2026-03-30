package com.auhpp.event_management.repository.custom;

import com.auhpp.event_management.constant.BookingType;
import com.auhpp.event_management.dto.request.DateRangeFilterRequest;
import com.auhpp.event_management.dto.response.RevenueChartResponse;

import java.util.List;

public interface BookingCustomRepository {
    List<RevenueChartResponse> getVoucherRevenueWithTimeLabel(
            Long eventSessionId, BookingType bookingType, DateRangeFilterRequest dateRangeFilterRequest);



}

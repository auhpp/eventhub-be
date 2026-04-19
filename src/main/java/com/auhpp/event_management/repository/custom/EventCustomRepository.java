package com.auhpp.event_management.repository.custom;

import com.auhpp.event_management.constant.EventStatus;
import com.auhpp.event_management.dto.request.DateRangeFilterRequest;
import com.auhpp.event_management.dto.response.CategoryDistributionResponse;
import com.auhpp.event_management.dto.response.EventApprovalStatResponse;

import java.util.List;

public interface EventCustomRepository {
    List<CategoryDistributionResponse> getCategoryDistribution(DateRangeFilterRequest request,
                                                                       EventStatus status);

    EventApprovalStatResponse getEventApprovalStatResponse(DateRangeFilterRequest request);



}

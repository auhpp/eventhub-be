package com.auhpp.event_management.repository.custom;

import com.auhpp.event_management.dto.request.DateRangeFilterRequest;
import com.auhpp.event_management.dto.request.PaginationFilterRequest;
import com.auhpp.event_management.dto.response.ResaleOverviewResponse;
import com.auhpp.event_management.dto.response.TopResaleEventResponse;

import java.util.List;

public interface ResaleCustomRepository {
    List<TopResaleEventResponse> getTopResaleEvent(DateRangeFilterRequest request,
                                                   PaginationFilterRequest paginationFilterRequest);
    ResaleOverviewResponse getResaleOverviewResponse(DateRangeFilterRequest request, Long eventId);

}

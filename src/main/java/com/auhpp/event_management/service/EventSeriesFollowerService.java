package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.EventSeriesFollowerCreateRequest;
import com.auhpp.event_management.dto.request.EventSeriesFollowerSearchRequest;
import com.auhpp.event_management.dto.response.EventSeriesFollowerResponse;
import com.auhpp.event_management.dto.response.PageResponse;

public interface EventSeriesFollowerService {
    EventSeriesFollowerResponse createEventSeriesFollower(EventSeriesFollowerCreateRequest request);

    void deleteEventSeriesFollower(Long id);

    PageResponse<EventSeriesFollowerResponse> getEventSeriesFollowers(EventSeriesFollowerSearchRequest request,
                                                                      int page, int size);

    Integer countEventSeriesFollowers(EventSeriesFollowerSearchRequest request);

}

package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.EventSeriesCreateRequest;
import com.auhpp.event_management.dto.request.EventSeriesSearchRequest;
import com.auhpp.event_management.dto.request.EventSeriesUpdateRequest;
import com.auhpp.event_management.dto.response.EventSeriesResponse;
import com.auhpp.event_management.dto.response.PageResponse;

import java.util.List;

public interface EventSeriesService {
    EventSeriesResponse createEventSeries(EventSeriesCreateRequest request);

    void deleteEventSeries(Long id);

    EventSeriesResponse updateEventSeries(Long id, EventSeriesUpdateRequest request);

    PageResponse<EventSeriesResponse> getEventSeries(EventSeriesSearchRequest request, int page, int size);

    EventSeriesResponse getEventSeriesById(Long id);

    List<EventSeriesResponse> getAllEventSeries();
}

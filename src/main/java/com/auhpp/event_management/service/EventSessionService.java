package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.EventSessionCreateRequest;
import com.auhpp.event_management.dto.request.EventSessionUpdateRequest;
import com.auhpp.event_management.dto.response.EventSessionResponse;
import com.auhpp.event_management.dto.response.EventSessionReportCheckInResponse;

public interface EventSessionService {
    EventSessionResponse createEventSession(EventSessionCreateRequest eventSessionCreateRequest, Long eventId);

    EventSessionResponse getEventSessionById(Long id);

    EventSessionResponse updateEventSession(Long id, EventSessionUpdateRequest request);

    void deleteById(Long id);

    EventSessionReportCheckInResponse reportCheckIn(Long id);
}

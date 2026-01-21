package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.EventSessionCreateRequest;
import com.auhpp.event_management.dto.response.EventSessionResponse;

public interface EventSessionService {
    EventSessionResponse createEventSession(EventSessionCreateRequest eventSessionCreateRequest, Long eventId);
}

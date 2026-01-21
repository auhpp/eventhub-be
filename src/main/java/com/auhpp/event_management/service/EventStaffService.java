package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.EventStaffCreateRequest;
import com.auhpp.event_management.dto.response.EventStaffResponse;

public interface EventStaffService {
    EventStaffResponse createEventStaff(EventStaffCreateRequest eventStaffCreateRequest);
}

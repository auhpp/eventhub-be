package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.AttendeeCreateRequest;
import com.auhpp.event_management.dto.request.AttendeeSearchRequest;
import com.auhpp.event_management.dto.response.AttendeeResponse;
import com.auhpp.event_management.dto.response.PageResponse;

public interface AttendeeService {
    AttendeeResponse createAttendee(AttendeeCreateRequest attendeeCreateRequest);

    AttendeeResponse confirmValidAttendee(Long attendeeId);

    PageResponse<AttendeeResponse> getAttendeesByCurrentUser(AttendeeSearchRequest attendeeSearchRequest,
                                                             int page, int size);

    AttendeeResponse getAttendeeById(Long id);

    AttendeeResponse assignAttendeeEmail(Long id, String email);

    String getMeetingLink(Long attendeeId);

}

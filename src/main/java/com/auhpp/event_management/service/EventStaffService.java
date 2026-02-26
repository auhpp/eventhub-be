package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.EventInvitationRejectRequest;
import com.auhpp.event_management.dto.request.EventStaffCreateRequest;
import com.auhpp.event_management.dto.request.EventStaffSearchRequest;
import com.auhpp.event_management.dto.response.EventStaffInvitationResponse;
import com.auhpp.event_management.dto.response.EventStaffResponse;
import com.auhpp.event_management.dto.response.PageResponse;

import java.util.List;

public interface EventStaffService {
    List<EventStaffInvitationResponse> createEventStaff(EventStaffCreateRequest eventStaffCreateRequest);

    EventStaffResponse acceptInvitation(String token);

    EventStaffResponse rejectInvitation(String token, EventInvitationRejectRequest eventInvitationRejectRequest);

    EventStaffResponse revokeInvitation(Long id);

    PageResponse<EventStaffResponse> getEventStaffs(EventStaffSearchRequest request,
                                                    int page, int size);

    EventStaffResponse getByToken(String token);

    EventStaffResponse getById(Long id);

}

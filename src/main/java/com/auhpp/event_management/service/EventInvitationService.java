package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.EventInvitationCreateRequest;
import com.auhpp.event_management.dto.request.EventInvitationRejectRequest;
import com.auhpp.event_management.dto.request.EventInvitationSearchRequest;
import com.auhpp.event_management.dto.response.EventInvitationResponse;
import com.auhpp.event_management.dto.response.PageResponse;

import java.util.List;

public interface EventInvitationService {
    List<EventInvitationResponse> createEventInvitation(EventInvitationCreateRequest eventInvitationCreateRequest);

    EventInvitationResponse acceptEventInvitation(String token);

    EventInvitationResponse rejectEventInvitation(String token, EventInvitationRejectRequest eventInvitationRejectRequest);

    PageResponse<EventInvitationResponse> getEventInvitations(EventInvitationSearchRequest request, int page, int size);

    EventInvitationResponse getByToken(String token);

    void revokeEventInvitation(Long id);

    void cleanupExpiredEventInvitations();
}

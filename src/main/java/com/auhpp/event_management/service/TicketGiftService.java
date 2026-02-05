package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.EventInvitationRejectRequest;
import com.auhpp.event_management.dto.request.TicketGiftCreateRequest;
import com.auhpp.event_management.dto.request.TicketGiftSearchRequest;
import com.auhpp.event_management.dto.response.AttendeeBasicResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.TicketGiftResponse;

import java.util.List;

public interface TicketGiftService {
    TicketGiftResponse createTicketGift(TicketGiftCreateRequest request);

    TicketGiftResponse accept(Long id);

    TicketGiftResponse reject(Long id, EventInvitationRejectRequest eventInvitationRejectRequest);

    TicketGiftResponse revoke(Long id);

    PageResponse<TicketGiftResponse> getTicketGifts(TicketGiftSearchRequest request,
                                                    int page, int size);

    TicketGiftResponse getById(Long id);

    List<AttendeeBasicResponse> getAttendees(Long id);

    void cleanupExpiredTicketGifts();

}

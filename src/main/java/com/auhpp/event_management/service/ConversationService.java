package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.ConversationCreateRequest;
import com.auhpp.event_management.dto.request.ConversationSearchRequest;
import com.auhpp.event_management.dto.request.ConversationUpdateRequest;
import com.auhpp.event_management.dto.response.ConversationResponse;
import com.auhpp.event_management.dto.response.PageResponse;

public interface ConversationService {
    ConversationResponse create(ConversationCreateRequest request);

    PageResponse<ConversationResponse> getConversations(ConversationSearchRequest request, int page, int size);

    ConversationResponse findById(Long id);

    ConversationResponse update(Long id, ConversationUpdateRequest request);

    void delete(Long id);
    ConversationResponse findByOtherMember(Long otherMemberId);

}

package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.CountUnseenMessageRequest;
import com.auhpp.event_management.dto.request.MessageCreateRequest;
import com.auhpp.event_management.dto.request.MessageSearchRequest;
import com.auhpp.event_management.dto.response.MessageResponse;
import com.auhpp.event_management.dto.response.PageResponse;

public interface MessageService {
    MessageResponse create(MessageCreateRequest request);

    void seen(Long id);

    void receive(Long id);

    Long countUnSeenMessage(CountUnseenMessageRequest request);

    void seenByConversation(Long conversationId);

    void receiveAll();

    PageResponse<MessageResponse> getMessages(MessageSearchRequest request, int page, int size);

}

package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.UserNoteCreateRequest;
import com.auhpp.event_management.dto.request.UserNoteSearchRequest;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.UserNoteResponse;

public interface UserNoteService {
    UserNoteResponse create(UserNoteCreateRequest request);

    void delete(Long id);

    PageResponse<UserNoteResponse> filter(UserNoteSearchRequest request, int page, int size);
}

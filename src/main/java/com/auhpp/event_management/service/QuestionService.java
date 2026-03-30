package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.QuestionCreateRequest;
import com.auhpp.event_management.dto.request.QuestionSearchRequest;
import com.auhpp.event_management.dto.request.QuestionUpdateRequest;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.QuestionResponse;

public interface QuestionService {
    QuestionResponse create(QuestionCreateRequest request);

    QuestionResponse update(Long id, QuestionUpdateRequest request);

    void delete(Long id);

    void upvote(Long id);

    PageResponse<QuestionResponse> filter(QuestionSearchRequest request, int page, int size);

}

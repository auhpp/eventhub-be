package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.TagCreateRequest;
import com.auhpp.event_management.dto.request.TagSearchRequest;
import com.auhpp.event_management.dto.request.TagUpdateRequest;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.TagResponse;

import java.util.List;

public interface TagService {
    TagResponse create(TagCreateRequest request);

    TagResponse update(Long id, TagUpdateRequest request);

    void delete(Long id);

    PageResponse<TagResponse> filter(TagSearchRequest request, int page, int size);

    List<TagResponse> getAll(TagSearchRequest request);

}

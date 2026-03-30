package com.auhpp.event_management.service;

import com.auhpp.event_management.constant.ResalePostStatus;
import com.auhpp.event_management.dto.request.RejectionRequest;
import com.auhpp.event_management.dto.request.ResalePostCreateRequest;
import com.auhpp.event_management.dto.request.ResalePostSearchRequest;
import com.auhpp.event_management.dto.request.ResalePostUpdateRequest;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.ResalePostResponse;

public interface ResalePostService {
    ResalePostResponse create(ResalePostCreateRequest request);

    ResalePostResponse update(Long id, ResalePostUpdateRequest request);

    void approvePost(Long id);

    void cancelPost(Long id);

    void rejectPost(Long id, RejectionRequest request);

    void cancelPostByAdmin(Long id, RejectionRequest request);

    PageResponse<ResalePostResponse> filter(ResalePostSearchRequest request, int page, int size);

    ResalePostResponse getById(Long id);

    Integer count(ResalePostStatus status);

}

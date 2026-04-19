package com.auhpp.event_management.service;

import com.alibaba.excel.ExcelWriter;
import com.auhpp.event_management.dto.request.*;
import com.auhpp.event_management.dto.response.EventBasicResponse;
import com.auhpp.event_management.dto.response.EventResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import org.springframework.web.multipart.MultipartFile;

public interface EventService {
    EventResponse createEvent(EventCreateRequest eventCreateRequest, MultipartFile thumbnail,
                              MultipartFile poster);

    EventBasicResponse updateEvent(Long id, EventUpdateRequest request, MultipartFile thumbnail,
                                   MultipartFile poster);

    void approveEvent(Long id, EventApproveRequest eventApproveRequest);

    void rejectEvent(Long id, RejectionRequest rejectionRequest);

    PageResponse<EventResponse> getEvents(EventSearchRequest request, int page, int size);

    EventResponse getEventById(Long id);

    void cancelEvent(Long id);

    Integer countEvent(EventCountRequest request);

    void exportReportEvent(ExcelWriter excelWriter, EventSearchRequest request);

    Boolean hasResalable(Long id);

}

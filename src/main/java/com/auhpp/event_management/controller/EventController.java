package com.auhpp.event_management.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.auhpp.event_management.constant.EventSessionStatus;
import com.auhpp.event_management.dto.request.*;
import com.auhpp.event_management.dto.response.*;
import com.auhpp.event_management.service.EventService;
import com.auhpp.event_management.service.EventSessionService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;

@RestController
@RequestMapping("/api/v1/event")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventController {

    EventService eventService;
    EventSessionService eventSessionService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<EventResponse> createEvent(
            @RequestPart(value = "data") @Valid EventCreateRequest eventCreateRequest,
            @RequestPart(value = "thumbnail") MultipartFile thumbnail,
            @RequestPart(value = "poster") MultipartFile poster

    ) {
        EventResponse result = eventService.createEvent(eventCreateRequest, thumbnail, poster);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping("/approve/{eventId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> approveEvent(
            @PathVariable("eventId") Long id,
            @Valid @RequestBody EventApproveRequest eventApproveRequest
    ) {
        eventService.approveEvent(id, eventApproveRequest);
        return ResponseEntity
                .status(HttpStatus.OK).build();
    }

    @PostMapping("/reject/{eventId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> rejectEvent(
            @PathVariable("eventId") Long id,
            @Valid @RequestBody RejectionRequest rejectionRequest
    ) {
        eventService.rejectEvent(id, rejectionRequest);
        return ResponseEntity
                .status(HttpStatus.OK).build();
    }

    @PostMapping("/filter")
    public ResponseEntity<PageResponse<EventResponse>> getEvents(
            @RequestBody EventSearchRequest request,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<EventResponse> response = eventService.getEvents(request, page, size);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEventById(
            @PathVariable(name = "eventId") Long id
    ) {
        EventResponse response = eventService.getEventById(id);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/{eventId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<EventBasicResponse> updateEvent(
            @PathVariable(name = "eventId") Long id,
            @RequestPart(value = "data") @Valid EventUpdateRequest request,
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestPart(value = "poster", required = false) MultipartFile poster
    ) {
        EventBasicResponse response = eventService.updateEvent(id, request, thumbnail, poster);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }


    @PostMapping("/{eventId}/event-session")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<EventSessionResponse> createEventSession(
            @PathVariable(name = "eventId") Long id,
            @Valid @RequestBody EventSessionCreateRequest request
    ) {
        request.setStatus(EventSessionStatus.APPROVED);
        EventSessionResponse response = eventSessionService.createEventSession(request, id);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @PostMapping("/cancel/{eventId}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<Void> cancelEvent(
            @PathVariable("eventId") Long id
    ) {
        eventService.cancelEvent(id);
        return ResponseEntity
                .status(HttpStatus.OK).build();
    }

    @PostMapping("/count")
    public ResponseEntity<Integer> countEvent(
            @RequestBody EventCountRequest request
    ) {
        Integer result = eventService.countEvent(request);
        return ResponseEntity
                .status(HttpStatus.OK).body(result);
    }

    @GetMapping("/has-resalable/{id}")
    public ResponseEntity<Boolean> hasResalable(
            @PathVariable("id") Long id
    ) {
        Boolean result = eventService.hasResalable(id);
        return ResponseEntity
                .status(HttpStatus.OK).body(result);
    }

    @PostMapping("/reports/export")
    public void exportEvent(
            @RequestBody EventSearchRequest request,
            HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("danh_sach_su_kien", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), EventReportExportResponse.class).build();
            eventService.exportReportEvent(excelWriter, request);

        } catch (Exception e) {
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println("{ \"message\": \"Lỗi trong quá trình xuất Excel: " + e.getMessage() + "\" }");
        }
    }

}

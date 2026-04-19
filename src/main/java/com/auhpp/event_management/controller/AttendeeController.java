package com.auhpp.event_management.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.auhpp.event_management.constant.ActionType;
import com.auhpp.event_management.dto.request.*;
import com.auhpp.event_management.dto.response.*;
import com.auhpp.event_management.service.AttendeeService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;

@RestController
@RequestMapping("/api/v1/attendee")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttendeeController {
    AttendeeService attendeeService;

    @PostMapping("/current-user")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<PageResponse<AttendeeResponse>> getAttendeeByCurrentUser(
            @Valid @RequestBody AttendeeSearchRequest attendeeSearchRequest,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<AttendeeResponse> response = attendeeService.getAttendeesByCurrentUser(
                attendeeSearchRequest, page, size);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{attendeeId}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<AttendeeResponse> getAttendeeById(
            @PathVariable(name = "attendeeId") Long id
    ) {
        AttendeeResponse response = attendeeService.getAttendeeById(id);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{attendeeId}/assign/{email}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<AttendeeResponse> assignAttendeeEmail(
            @PathVariable(name = "attendeeId") Long id,
            @PathVariable(name = "email") String email
    ) {
        AttendeeResponse response = attendeeService.assignAttendeeEmail(id, email);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{attendeeId}/join-link")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<String> getMeetingUrl(
            @PathVariable(name = "attendeeId") Long id
    ) {
        String response = attendeeService.getMeetingLink(id);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @PostMapping("/check-in/filter")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<PageResponse<AttendeeResponse>> getAttendees(
            @Valid @RequestBody CheckinSearchRequest request,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<AttendeeResponse> response = attendeeService.getAttendees(request, page, size);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @PostMapping("/check-in")
    public ResponseEntity<AttendeeBasicResponse> checkIn(
            @Valid @RequestBody CheckInRequest request
    ) {
        AttendeeBasicResponse response = attendeeService.checkIn(request);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @PostMapping("/cancel/{attendeeId}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<AttendeeBasicResponse> cancelAttendee(
            @PathVariable(name = "attendeeId") Long id
    ) {
        AttendeeBasicResponse response = attendeeService.cancelAttendee(id);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @PostMapping("/event-session/{eventSessionId}/filter")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<PageResponse<UserAttendeeSummaryResponse>> getUserSummaryAttendees(
            @PathVariable(name = "eventSessionId") Long eventSessionId,
            @RequestBody AttendeeSearchRequest searchRequest,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<UserAttendeeSummaryResponse> response = attendeeService.getUserAttendeeSummaries(
                eventSessionId,
                searchRequest, page, size);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @GetMapping("/ticket-code/{attendeeId}")
    public ResponseEntity<String> getTicketCode(
            @PathVariable(name = "attendeeId") Long id
    ) {
        String response = attendeeService.getTicketCode(id);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @GetMapping("/ticket/{ticketId}/user/{userId}")
    public ResponseEntity<Integer> countBoughtTicket(
            @PathVariable(name = "ticketId") Long ticketId,
            @PathVariable(name = "userId") Long userId
    ) {
        int response = attendeeService.countBoughtTicket(ticketId, userId);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }


    @PostMapping("/check-in/import/{eventSessionId}")
    public ResponseEntity<AttendanceImportCheckInResponse> importAttendance(
            @PathVariable(name = "eventSessionId") Long id,
            @RequestBody AttendanceImportRequest request
    ) {
        AttendanceImportCheckInResponse response = attendeeService.processAttendanceFromEmails(id, request);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @PostMapping("/check-in/manual/{attendeeId}")
    public ResponseEntity<Void> checkInManual(
            @PathVariable("attendeeId") Long attendeeId,
            @RequestParam("actionType") ActionType actionType,
            @RequestParam("eventId") Long eventId
    ) {
        attendeeService.checkIn(CheckInRequest.builder().attendeeId(attendeeId).actionType(actionType)
                .eventId(eventId)
                .build());
        return ResponseEntity
                .status(HttpStatus.OK).build();
    }

    @GetMapping("/check-in/ticket/{ticketId}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<PageResponse<AttendeeCheckinResponse>> getCheckedIns(
            @PathVariable(name = "ticketId") Long ticketId,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<AttendeeCheckinResponse> response = attendeeService.getAttendeeCheckins(ticketId, email, page, size);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @PostMapping("/check-in/logs")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<PageResponse<CheckInLogResponse>> getCheckInLogs(
            @RequestBody CheckInLogSearchRequest request,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<CheckInLogResponse> response = attendeeService.getCheckInLogs(request, page, size);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @PostMapping("/reports/export")
    public void exportAttendees(
            @RequestBody AttendeeSearchRequest request,
            @RequestParam("eventName") String eventName,
            HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("danh_sach_nguoi_tham_gia", "UTF-8")
                    .replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), AttendeeExportResponse.class).build();
            attendeeService.exportReportAttendees(excelWriter, request, eventName);

        } catch (Exception e) {
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println("{ \"message\": \"Lỗi trong quá trình xuất Excel: " + e.getMessage() + "\" }");
        }
    }
}

package com.auhpp.event_management.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.auhpp.event_management.constant.BookingStatus;
import com.auhpp.event_management.dto.request.BookingSearchRequest;
import com.auhpp.event_management.dto.request.PendingBookingCreateRequest;
import com.auhpp.event_management.dto.request.PendingResaleBookingCreateRequest;
import com.auhpp.event_management.dto.response.*;
import com.auhpp.event_management.service.BookingService;
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
@RequestMapping("/api/v1/booking")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {
    BookingService bookingService;

    @PostMapping("/pending")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<BookingResponse> createPendingBooking(
            @Valid @RequestBody PendingBookingCreateRequest pendingBookingCreateRequest
    ) {
        BookingResponse result = bookingService.createPendingBooking(pendingBookingCreateRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping("/resale/pending")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<BookingResponse> createResalePendingBooking(
            @Valid @RequestBody PendingResaleBookingCreateRequest request
    ) {
        BookingResponse result = bookingService.createPendingResaleBooking(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping("/filter")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<PageResponse<BookingBasicResponse>> getBookings(
            @RequestBody BookingSearchRequest bookingSearchRequest,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<BookingBasicResponse> response = bookingService.getBookings(
                bookingSearchRequest, page, size);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{bookingId}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<BookingResponse> getBookingById(
            @PathVariable(name = "bookingId") Long id
    ) {
        BookingResponse response = bookingService.getBookingById(id);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{bookingId}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<Void> deleteBookingById(
            @PathVariable(name = "bookingId") Long id
    ) {
        bookingService.deleteBooking(id);
        return ResponseEntity
                .status(HttpStatus.OK).build();
    }

    @GetMapping("/event-session/{eventSessionId}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<BookingResponse> getExistsPendingBooking(
            @PathVariable(name = "eventSessionId") Long eventSessionId
    ) {
        BookingResponse response = bookingService.getBookingByEventSessionIdAndCurrentUserAndStatus(
                eventSessionId, BookingStatus.PENDING
        );
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @GetMapping("/resale/{resalePostId}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<BookingResponse> getExistsPendingResaleBooking(
            @PathVariable(name = "resalePostId") Long resalePostId
    ) {
        BookingResponse response = bookingService.getBookingByResalePostIdAndCurrentUserAndStatus(
                resalePostId, BookingStatus.PENDING
        );
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }


    @GetMapping("/event-session/{eventSessionId}/user/{userId}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<UserBookingSummaryResponse> getUserSummaryBooking(
            @PathVariable(name = "eventSessionId") Long eventSessionId,
            @PathVariable(name = "userId") Long userId
    ) {
        UserBookingSummaryResponse response = bookingService.getUserBookingSummary(
                eventSessionId,
                userId);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }

    @PostMapping("/reports/export")
    public void exportBookings(
            @RequestBody BookingSearchRequest bookingSearchRequest,
            @RequestParam("eventName") String eventName,
            HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("bao_bao_don_hang", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), BookingReportResponse.class).build();
            bookingService.exportReportBookings(excelWriter, bookingSearchRequest, eventName);

        } catch (Exception e) {
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println("{ \"message\": \"Lỗi trong quá trình xuất Excel: " + e.getMessage() + "\" }");
        }
    }
}

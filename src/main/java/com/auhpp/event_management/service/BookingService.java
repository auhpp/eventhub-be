package com.auhpp.event_management.service;

import com.alibaba.excel.ExcelWriter;
import com.auhpp.event_management.constant.BookingStatus;
import com.auhpp.event_management.constant.WalletType;
import com.auhpp.event_management.dto.request.*;
import com.auhpp.event_management.dto.response.BookingBasicResponse;
import com.auhpp.event_management.dto.response.BookingResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.UserBookingSummaryResponse;
import com.auhpp.event_management.entity.Booking;

import java.io.IOException;
import java.time.LocalDateTime;

public interface BookingService {
    BookingResponse createPendingBooking(PendingBookingCreateRequest pendingBookingCreateRequest);

    BookingResponse createPendingResaleBooking(PendingResaleBookingCreateRequest request);

    Booking createInvitationBooking(InvitationBookingCreateRequest bookingCreateRequest);

    void deleteBooking(Long id);

    BookingResponse updatePaymentInfoBooking(Long id, BookingPaymentRequest bookingPaymentRequest);

    Double calculateFinalAmount(Long id, BookingPaymentRequest bookingPaymentRequest);

    BookingResponse getBookingById(Long id);

    BookingResponse getBookingByTransactionId(String transactionId, WalletType walletType);

    BookingResponse updatePaymentBooking(Long id, LocalDateTime vnpPayDate) throws IOException;

    PageResponse<BookingBasicResponse> getBookings(BookingSearchRequest bookingSearchRequest,
                                                   int page, int size);

    void cleanupExpiredBookings();

    BookingResponse getBookingByEventSessionIdAndCurrentUserAndStatus(Long eventId, BookingStatus status);

    BookingResponse getBookingByResalePostIdAndCurrentUserAndStatus(Long resalePostId, BookingStatus status);

    UserBookingSummaryResponse getUserBookingSummary(Long eventSessionId, Long userId);

    void exportReportBookings(ExcelWriter excelWriter, BookingSearchRequest request, String eventName);

}

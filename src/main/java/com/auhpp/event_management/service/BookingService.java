package com.auhpp.event_management.service;

import com.auhpp.event_management.constant.BookingStatus;
import com.auhpp.event_management.constant.WalletType;
import com.auhpp.event_management.dto.request.BookingPaymentRequest;
import com.auhpp.event_management.dto.request.BookingSearchRequest;
import com.auhpp.event_management.dto.request.InvitationBookingCreateRequest;
import com.auhpp.event_management.dto.request.PendingBookingCreateRequest;
import com.auhpp.event_management.dto.response.BookingResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.UserBookingSummaryResponse;
import com.auhpp.event_management.entity.Booking;

import java.time.LocalDateTime;

public interface BookingService {
    BookingResponse createPendingBooking(PendingBookingCreateRequest pendingBookingCreateRequest);

    Booking createInvitationBooking(InvitationBookingCreateRequest bookingCreateRequest);

    void deleteBooking(Long id);

    BookingResponse updatePaymentInfoBooking(Long id, BookingPaymentRequest bookingPaymentRequest);

    Double calculateFinalAmount(Long id, BookingPaymentRequest bookingPaymentRequest);

    BookingResponse getBookingById(Long id);

    BookingResponse getBookingByTransactionId(String transactionId, WalletType walletType);

    BookingResponse updatePaymentBooking(Long id, LocalDateTime vnpPayDate);

    PageResponse<BookingResponse> getBookings(BookingSearchRequest bookingSearchRequest,
                                              int page, int size);

    void cleanupExpiredBookings();

    BookingResponse getBookingByEventSessionIdAndCurrentUserAndStatus(Long eventId, BookingStatus status);

    UserBookingSummaryResponse getUserBookingSummary(Long eventSessionId, Long userId);
}

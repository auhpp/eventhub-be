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

public interface BookingService {
    BookingResponse createPendingBooking(PendingBookingCreateRequest pendingBookingCreateRequest);

    Booking createInvitationBooking(InvitationBookingCreateRequest bookingCreateRequest);

    void deleteBooking(Long id);

    BookingResponse updatePaymentInfoBooking(Long id, BookingPaymentRequest bookingPaymentRequest);

    Double calculateFinalAmount(Long id, BookingPaymentRequest bookingPaymentRequest);

    BookingResponse getBookingById(Long id);

    BookingResponse getBookingByTransactionId(String transactionId, WalletType walletType);

    BookingResponse updatePaymentBooking(Long id);

    PageResponse<BookingResponse> getBookingsByCurrentUser(BookingSearchRequest bookingSearchRequest,
                                                           int page, int size);

    void cleanupExpiredBookings();

    BookingResponse getBookingByEventSessionIdAndCurrentUserAndStatus(Long eventId, BookingStatus status);

    PageResponse<UserBookingSummaryResponse> getUserBookingSummaries(Long eventSessionId,
                                                                     BookingSearchRequest bookingSearchRequest,
                                                                     int page, int size);

    UserBookingSummaryResponse getUserBookingSummary(Long eventSessionId, Long userId);
}

package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.BookingPaymentRequest;
import com.auhpp.event_management.dto.request.CheckPaymentRequest;
import com.auhpp.event_management.dto.response.BookingConfirmPaymentResponse;
import com.auhpp.event_management.dto.response.RefundBookingResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface VNPayService {
    //Build redirect payment URL
    String createPaymentUrl(Long bookingId, BookingPaymentRequest request,
                            HttpServletRequest httpServletRequest) throws UnsupportedEncodingException;

    BookingConfirmPaymentResponse confirmPaymentBooking(CheckPaymentRequest checkPaymentRequest,
                                                        HttpServletRequest httpServletRequest);

    List<RefundBookingResponse> refund(Long eventId, HttpServletRequest httpServletRequest);
}

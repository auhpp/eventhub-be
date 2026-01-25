package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.BookingPaymentRequest;
import com.auhpp.event_management.dto.request.CheckPaymentRequest;
import com.auhpp.event_management.dto.response.BookingConfirmPaymentResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;

public interface VNPayService {
    //Build redirect payment URL
    String createPaymentUrl(Long bookingId, BookingPaymentRequest request,
                            HttpServletRequest httpServletRequest) throws UnsupportedEncodingException;

    BookingConfirmPaymentResponse confirmPaymentBooking(CheckPaymentRequest checkPaymentRequest, HttpServletRequest httpServletRequest);
}

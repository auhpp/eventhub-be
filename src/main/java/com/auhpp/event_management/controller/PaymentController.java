package com.auhpp.event_management.controller;

import com.auhpp.event_management.dto.request.BookingPaymentRequest;
import com.auhpp.event_management.dto.request.CheckPaymentRequest;
import com.auhpp.event_management.dto.response.BookingConfirmPaymentResponse;
import com.auhpp.event_management.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {
    VNPayService vnPayService;

    @PostMapping("/create-payment-url/{bookingId}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<String> createPayment(
            @PathVariable("bookingId") Long bookingId,
            @Valid @RequestBody BookingPaymentRequest request, HttpServletRequest httpServletRequest
    ) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(vnPayService.createPaymentUrl(bookingId, request, httpServletRequest));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating payment URL: " + e.getMessage());
        }
    }

    @PostMapping("/confirm-payment-order")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<BookingConfirmPaymentResponse> confirmPaymentBooking(
            @Valid @RequestBody CheckPaymentRequest checkPaymentRequest, HttpServletRequest httpServletRequest
    ) {
        BookingConfirmPaymentResponse response = vnPayService.confirmPaymentBooking(checkPaymentRequest, httpServletRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

}

package com.auhpp.event_management.controller;

import com.auhpp.event_management.dto.request.CouponCreateRequest;
import com.auhpp.event_management.dto.request.CouponSearchRequest;
import com.auhpp.event_management.dto.request.CouponUpdateRequest;
import com.auhpp.event_management.dto.response.CouponReportDetailResponse;
import com.auhpp.event_management.dto.response.CouponResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.service.CouponService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coupon")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CouponController {
    CouponService couponService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<CouponResponse> createCoupon(
            @Valid @ModelAttribute CouponCreateRequest request
    ) {
        CouponResponse result = couponService.createCoupon(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PutMapping(path = "/{couponId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<CouponResponse> updateCoupon(
            @PathVariable("couponId") Long couponId,
            @Valid @ModelAttribute CouponUpdateRequest request
    ) {
        CouponResponse result = couponService.updateCoupon(couponId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @DeleteMapping(path = "/{couponId}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<Void> deleteCoupon(
            @PathVariable("couponId") Long couponId
    ) {
        couponService.deleteCoupon(couponId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping
    public ResponseEntity<PageResponse<CouponResponse>> getCoupons(
            @RequestBody CouponSearchRequest request,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<CouponResponse> result = couponService.getCoupons(request, page, size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @GetMapping("/report/{couponId}")
    public ResponseEntity<List<CouponReportDetailResponse>> getInfoReportDetail(
            @PathVariable("couponId") Long couponId

    ) {
        List<CouponReportDetailResponse> result = couponService.getInfoReportDetail(couponId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping("/exists/{eventId}")
    public ResponseEntity<CouponResponse> getByCode(
            @PathVariable("eventId") Long eventId,
            @RequestParam("code") String code
    ) {
        CouponResponse result = couponService.getByCode(eventId, code);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @DeleteMapping(path = "/{couponId}/ticket-coupon/{ticketCouponId}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<Void> deleteTicketCoupon(
            @PathVariable("ticketCouponId") Long ticketCouponId
    ) {
        couponService.deleteTicketCoupon(ticketCouponId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }


    @GetMapping(path = "/{couponId}/count-booking/{userId}")
    public ResponseEntity<Integer> countBookingByUserId(
            @PathVariable("userId") Long userId,
            @PathVariable("couponId") Long couponId
    ) {
        Integer cnt = couponService.cntBookingByUser(userId, couponId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cnt);
    }

    @GetMapping(path = "/{couponId}")
    public ResponseEntity<CouponResponse> getById(
            @PathVariable("couponId") Long couponId
    ) {
        CouponResponse cnt = couponService.getById(couponId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cnt);
    }
}

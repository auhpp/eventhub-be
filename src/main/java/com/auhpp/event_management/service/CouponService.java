package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.CouponCreateRequest;
import com.auhpp.event_management.dto.request.CouponSearchRequest;
import com.auhpp.event_management.dto.request.CouponUpdateRequest;
import com.auhpp.event_management.dto.response.CouponReportDetailResponse;
import com.auhpp.event_management.dto.response.CouponResponse;
import com.auhpp.event_management.dto.response.PageResponse;

import java.util.List;

public interface CouponService {
    CouponResponse createCoupon(CouponCreateRequest request);

    CouponResponse updateCoupon(Long couponId, CouponUpdateRequest request);

    void deleteCoupon(Long couponId);

    PageResponse<CouponResponse> getCoupons(CouponSearchRequest request, int page, int size);

    CouponReportDetailResponse getInfoReportDetail(Long couponId);

    CouponResponse getByCode(Long eventId, String code);

    Integer cntBookingByUser(Long userId, Long couponId);

    void deleteTicketCoupon(Long ticketCouponId);

    CouponResponse getById(Long id);

    void disableCoupon(Long id);
}

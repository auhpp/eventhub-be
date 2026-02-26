package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.CommonStatus;
import com.auhpp.event_management.constant.CouponStatus;
import com.auhpp.event_management.constant.FolderName;
import com.auhpp.event_management.dto.request.CouponCreateRequest;
import com.auhpp.event_management.dto.request.CouponSearchRequest;
import com.auhpp.event_management.dto.request.CouponUpdateRequest;
import com.auhpp.event_management.dto.response.CouponReportDetailResponse;
import com.auhpp.event_management.dto.response.CouponResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.entity.Coupon;
import com.auhpp.event_management.entity.Ticket;
import com.auhpp.event_management.entity.TicketCoupon;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.CouponMapper;
import com.auhpp.event_management.mapper.TicketStandardMapper;
import com.auhpp.event_management.repository.CouponRepository;
import com.auhpp.event_management.repository.TicketCouponRepository;
import com.auhpp.event_management.repository.TicketRepository;
import com.auhpp.event_management.service.CloudinaryService;
import com.auhpp.event_management.service.CouponService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    CouponRepository couponRepository;
    CouponMapper couponMapper;
    CloudinaryService cloudinaryService;
    TicketRepository ticketRepository;
    TicketStandardMapper ticketStandardMapper;
    TicketCouponRepository ticketCouponRepository;

    @Override
    @Transactional
    public CouponResponse createCoupon(CouponCreateRequest request) {

        if (couponRepository.findByCodeAndEventId(request.getCode(), request.getEventId()).isPresent()) {
            throw new AppException(ErrorCode.CODE_EXISTS);
        }
        if (request.getEndDateTime().isBefore(request.getStartDateTime())) {
            throw new AppException(ErrorCode.INVALID_PARAMS);
        }
        Coupon coupon = couponMapper.toCoupon(request);
        List<TicketCoupon> ticketCoupons = new ArrayList<>();
        if (request.getTicketIds() == null || request.getTicketIds().isEmpty()) {
            List<Ticket> tickets = ticketRepository.findByEventId(request.getEventId());
            for (Ticket ticket : tickets) {
                ticketCoupons.add(TicketCoupon.builder()
                        .coupon(coupon)
                        .status(CommonStatus.ACTIVE)
                        .ticket(ticket)
                        .build());
            }
        } else {
            for (Long ticketId : request.getTicketIds()) {
                Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(
                        () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
                );
                ticketCoupons.add(TicketCoupon.builder()
                        .coupon(coupon)
                        .status(CommonStatus.ACTIVE)
                        .ticket(ticket)
                        .build());
            }
        }
        coupon.setTicketCoupons(ticketCoupons);
        coupon.setStatus(CouponStatus.ACTIVE);
        couponRepository.save(coupon);

        // upload image
        if (request.getAvatar() != null) {
            Map<String, Object> uploadResult = cloudinaryService.uploadFile(request.getAvatar(),
                    FolderName.COUPON_AVATAR.getValue() + coupon.getId());
            String publicId = (String) uploadResult.get("public_id");
            String imageUrl = (String) uploadResult.get("secure_url");
            coupon.setAvatarPublicId(publicId);
            coupon.setAvatarUrl(imageUrl);
            couponRepository.save(coupon);
        }
        return couponMapper.toCouponResponse(coupon);
    }

    @Override
    @Transactional
    public CouponResponse updateCoupon(Long couponId, CouponUpdateRequest request) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        couponMapper.updateCoupon(request, coupon);
        List<TicketCoupon> ticketCoupons = new ArrayList<>();
        for (Long ticketId : request.getTicketIds()) {
            if (ticketCouponRepository.findByTicketIdAndCouponId(ticketId, couponId).isPresent()) {
                continue;
            }
            Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(
                    () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
            );
            ticketCoupons.add(TicketCoupon.builder()
                    .coupon(coupon)
                    .status(CommonStatus.ACTIVE)
                    .ticket(ticket)
                    .build());
        }
        coupon.setTicketCoupons(ticketCoupons);
        if (request.getAvatar() != null) {
            if (coupon.getAvatarUrl() != null && !coupon.getAvatarUrl().isEmpty()) {
                cloudinaryService.deleteFile(coupon.getAvatarPublicId());
            }
            Map<String, Object> uploadResult = cloudinaryService.uploadFile(request.getAvatar(),
                    FolderName.COUPON_AVATAR.getValue() + coupon.getId());
            String publicId = (String) uploadResult.get("public_id");
            String imageUrl = (String) uploadResult.get("secure_url");
            coupon.setAvatarPublicId(publicId);
            coupon.setAvatarUrl(imageUrl);
        }
        couponRepository.save(coupon);
        return couponMapper.toCouponResponse(coupon);
    }

    @Override
    @Transactional
    public void deleteCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        boolean valid = true;
        for (TicketCoupon ticketCoupon : coupon.getTicketCoupons()) {
            if (!ticketCoupon.getCouponUsages().isEmpty()) {
                valid = false;
                break;
            }
        }
        if (valid) {
            if (coupon.getAvatarUrl() != null && !coupon.getAvatarUrl().isEmpty()) {
                cloudinaryService.deleteFile(coupon.getAvatarPublicId());
            }
            couponRepository.delete(coupon);
        } else {
            throw new AppException(ErrorCode.RESOURCE_CAN_NOT_DELETE);
        }
    }

    @Override
    public PageResponse<CouponResponse> getCoupons(CouponSearchRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC,
                "createdAt"));
        Page<Coupon> pageData = couponRepository.filterCoupon(request.getEventId(), pageable);
        List<CouponResponse> responses = pageData.getContent().stream().map(
                couponMapper::toCouponResponse
        ).toList();
        return PageResponse.<CouponResponse>builder()
                .currentPage(page)
                .totalElements(pageData.getTotalElements())
                .totalPage(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .data(responses)
                .build();
    }

    @Override
    public List<CouponReportDetailResponse> getInfoReportDetail(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        List<CouponReportDetailResponse> responses = new ArrayList<>();
        for (TicketCoupon ticketCoupon : coupon.getTicketCoupons()) {
            responses.add(CouponReportDetailResponse.builder()
                    .ticket(ticketStandardMapper.toTicketStandardResponse(ticketCoupon.getTicket()))
                    .usageQuantity(ticketCoupon.getCouponUsages().size())
                    .build());
        }
        return responses;
    }

    @Override
    public boolean existsCode(Long eventId, String code) {
        return couponRepository.findByCodeAndEventId(code, eventId).isPresent();
    }
}

package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.CouponStatus;
import com.auhpp.event_management.constant.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponResponse {
    private Long id;

    private String name;

    private CouponStatus status;

    private String code;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private String content;

    private String avatarUrl;

    private DiscountType discountType;

    private Double value;

    private Integer maxDiscountAmount;

    private Integer quantityUsage;

    private Integer maximumUsage;

    private Integer maximumBooking;

    private Integer minimumTicketInBooking;

    private Integer maximumTicketInBooking;

    private Boolean hasPublic;

    private List<TicketBasicResponse> tickets;
}

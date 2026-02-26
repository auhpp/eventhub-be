package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.DiscountType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    private String code;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private String content;

    private String avatarUrl;

    private DiscountType discountType;

    private Double value;

    private Integer maximumTicket;

    private Integer maximumBooking;

    private Integer minimumTicket;

    private Integer maximumTicketInBooking;

    private Boolean hasPublic;

    private List<EventSessionBasicResponse> eventSessions;
}

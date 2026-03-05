package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponUpdateRequest {
    private Long eventId;

    private String name;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private String content;

    private MultipartFile avatar;

    private DiscountType discountType;

    private Double value;

    private Integer maxDiscountAmount;

    private Integer maximumUsage;

    private Integer maximumBooking;

    private Integer minimumTicketInBooking;

    private Integer maximumTicketInBooking;

    private Boolean hasPublic;

    private List<Long> ticketIds;


}

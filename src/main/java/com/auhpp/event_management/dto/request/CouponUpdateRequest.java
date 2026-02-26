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
    private String name;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private String content;

    private MultipartFile avatar;

    private DiscountType discountType;

    private Double value;

    private Integer maximumTicket;

    private Integer maximumBooking;

    private Integer minimumTicket;

    private Integer maximumTicketInBooking;

    private Boolean hasPublic;

    private List<Long> ticketIds;


}

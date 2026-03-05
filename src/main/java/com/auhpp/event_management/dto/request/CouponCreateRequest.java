package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.DiscountType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class CouponCreateRequest {
    @NotNull(message = "Event id cannot null")
    private Long eventId;

    @NotEmpty(message = "Coupon name cannot empty")
    private String name;

    @NotEmpty(message = "Coupon name cannot empty")
    @Size(min = 6, max = 12, message = "Coupon code must be between 6 and 12 characters long")
    private String code;

    @NotNull(message = "Start date time cannot null")
    private LocalDateTime startDateTime;

    @NotNull(message = "End date time cannot null")
    private LocalDateTime endDateTime;

    private String content;

    private MultipartFile avatar;

    @NotNull(message = "Discount type cannot null")
    private DiscountType discountType;

    @NotNull(message = "Value cannot null")
    private Double value;

    private Integer maxDiscountAmount;

    private Integer maximumUsage;

    @NotNull(message = "Maximum booking cannot null")
    private Integer maximumBooking;

    @NotNull(message = "Minimum ticket cannot null")
    private Integer minimumTicketInBooking;

    @NotNull(message = "Maximum ticket in booking cannot null")
    private Integer maximumTicketInBooking;

    @NotNull(message = "Maximum ticket in booking cannot null")
    private Boolean hasPublic;

    private List<Long> ticketIds;
}

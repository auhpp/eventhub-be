package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.BookingStatus;
import com.auhpp.event_management.constant.WalletType;
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
public class BookingResponse {
    private Long id;

    private Double totalAmount;

    private Double discountAmount;

    private Double finalAmount;

    private BookingStatus status;

    private String transactionId;

    private String customerName;

    private String customerEmail;

    private String customerPhone;

    private String note;

    private WalletType walletType;

    private LocalDateTime expiredAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<AttendeeResponse> attendees;

    private UserResponse appUser;

    private EventResponse event;
}

package com.auhpp.event_management.dto.response;


import com.auhpp.event_management.constant.AttendeeType;
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
public class BookingBasicResponse {
    private Long id;

    private Double finalAmount;

    private BookingStatus status;

    private String customerName;

    private String customerEmail;

    private String customerPhone;

    private AttendeeType type;

    private WalletType walletType;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<AttendeeBasicResponse> attendees;

    private EventBasicResponse event;

    private EventSessionBasicResponse eventSession;

}

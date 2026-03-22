package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.ResalePostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResalePostResponse {
    private Long id;

    private Double pricePerTicket;

    private ResalePostStatus status;

    private Boolean hasRetail;

    private String rejectionMessage;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private UserBasicResponse appUser;

    private List<AttendeeBasicResponse> attendees;
}

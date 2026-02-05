package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.EventStaffStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventStaffResponse {
    private Long id;
    private String fullName;
    private String email;
    private RoleResponse role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime expiredAt;
    private EventStaffStatus status;
    private String token;
    private Long eventId;
}

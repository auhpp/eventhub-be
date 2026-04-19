package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.ActionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInLogResponse {
    private Long id;

    private ActionType actionType;

    private LocalDateTime createdAt;

    private Long attendeeId;

    private Long eventStaffId;

    private String eventStaffEmail;

    private String eventStaffFullName;

}

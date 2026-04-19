package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.AttendeeSearchStatus;
import com.auhpp.event_management.constant.AttendeeStatus;
import com.auhpp.event_management.constant.AttendeeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendeeSearchRequest {
    private List<AttendeeStatus> statuses;
    private AttendeeSearchStatus searchStatus;
    private List<AttendeeType> types;
    private String email;
    private String name;
    private Long ticketId;
    private Long eventSessionId;
}

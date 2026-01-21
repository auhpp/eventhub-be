package com.auhpp.event_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventStaffResponse {
    private String fullName;
    private String email;
    private RoleResponse role;
}

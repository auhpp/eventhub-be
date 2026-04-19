package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.RoleName;
import com.auhpp.event_management.validation.annotation.ValidUpdateEventSession;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidUpdateEventSession
public class EventStaffChangeRoleRequest {
    @NotNull(message = "Role name cannot null")
    private RoleName roleName;

}

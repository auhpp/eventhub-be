package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.RoleName;
import com.auhpp.event_management.constant.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponse {
    Long id;
    RoleName name;
    String description;
    RoleType type;
}

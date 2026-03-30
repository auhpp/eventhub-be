package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.RoleName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchRequest {
    private String email;

    private Boolean status;

    private RoleName roleName;
}

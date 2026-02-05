package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.RoleName;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventStaffCreateRequest {
    @NotEmpty(message = "Email cannot be empty")
    private Set<String> emails;

    @NotNull(message = "Event id cannot be null")
    private Long eventId;

    @NotNull(message = "Event role name cannot be null")
    private RoleName roleName;

    private String message;

    private LocalDateTime expiredAt;

}

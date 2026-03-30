package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.RegistrationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizerRegistrationSearchRequest {
    private Long userId;

    private String email;

    private String organizerName;

    private RegistrationStatus status;
}

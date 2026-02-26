package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.RegistrationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizerRegistrationResponse {
    private Long id;

    private String businessName;

    private String businessAvatarUrl;

    private String representativeFullName;

    private String phoneNumber;

    private String email;

    private String biography;

    private String contactAddress;

    private RegistrationStatus status;

    private String rejectionReason;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private UserResponse appUser;
}

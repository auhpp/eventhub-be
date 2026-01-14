package com.auhpp.event_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;

    private String fullName;

    private String phoneNumber;

    private String avatar;

    private String biography;

    private String email;

    private Boolean isOnline;

    private LocalDateTime lastSeen;

    private Boolean status;

    private String authProvider;

    private String providerId;

    private LocalDateTime createdAt;

    private RoleResponse role;

}

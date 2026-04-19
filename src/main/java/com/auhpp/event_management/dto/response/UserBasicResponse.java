package com.auhpp.event_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBasicResponse {
    private Long id;
    private String email;
    private String fullName;
    private String avatar;
    private String biography;
    private Boolean isOnline;
    private Boolean status;
    private LocalDateTime lastSeen;
    private LocalDateTime createdAt;
    private RoleResponse role;
    private List<SocialLinkResponse> socialLinks;
}

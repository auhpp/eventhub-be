package com.auhpp.event_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFollowerResponse {
    private Long id;
    private UserBasicResponse follower;
    private UserBasicResponse followed;
    private LocalDateTime createdAt;
}

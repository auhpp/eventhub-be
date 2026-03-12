package com.auhpp.event_management.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFollowerCreateRequest {
    @NotNull(message = "Follower id cannot null")
    private Long followerId;

    @NotNull(message = "followed id cannot null")
    private Long followedId;
}

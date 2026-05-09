package com.auhpp.event_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {
    private Long id;
    private String comment;
    private String replyMessage;
    private LocalDateTime replyAt;
    private Integer rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserBasicResponse user;
    private int editCount;
    private List<ReviewImageResponse> reviewImages;
    private String fullNameEventStaff;
    private String emailEventStaff;
    private String avatarEventStaff;
    private String eventName;
    private String thumbnailUrl;
    private Long eventId;
    private LocalDateTime startDateTime;

}

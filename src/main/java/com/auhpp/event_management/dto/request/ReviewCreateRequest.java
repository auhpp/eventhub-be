package com.auhpp.event_management.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateRequest {

    @NotNull(message = "Event session id cannot null")
    private Long eventSessionId;

    @NotNull(message = "Attendee id cannot null")
    private Long attendeeId;

    private String comment;

    @NotNull(message = "rating cannot null")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 1000")
    private Integer rating;

    private List<MultipartFile> files;
}

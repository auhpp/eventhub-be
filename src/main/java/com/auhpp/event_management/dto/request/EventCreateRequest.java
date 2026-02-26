package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.EventType;
import com.auhpp.event_management.constant.MeetingPlatform;
import com.auhpp.event_management.validation.annotation.ValidCreateEvent;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@ValidCreateEvent
public class EventCreateRequest {
    @NotEmpty(message = "Event name cannot be empty")
    private String name;

    @NotNull(message = "Event type cannot be null")
    private EventType type;

    @NotNull(message = "Category id cannot be null")
    private Long categoryId;

    @NotEmpty(message = "Event name cannot be empty")
    private String description;

    private String address;

    private String location;

    private Double locationLongitude;

    private Double locationLatitude;

    @NotEmpty(message = "Event session cannot be empty")
    @Size(min = 1)
    private List<@Valid EventSessionCreateRequest> eventSessionCreateRequests;


}

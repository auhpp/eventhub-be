package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.validation.annotation.ValidUpdateEvent;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidUpdateEvent
public class EventUpdateRequest {
    private String name;

    private Long categoryId;

    private Long eventSeriesId;

    private String description;

    private String address;

    private String location;

    private Double locationLongitude;

    private Double locationLatitude;

    private Boolean hasResalable;

    @NotEmpty(message = "tags cannot be empty")
    @Size(min = 1)
    private List<@Valid TagUpdateRequest> tags;
}

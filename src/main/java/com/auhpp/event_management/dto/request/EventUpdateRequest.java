package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.validation.annotation.ValidUpdateEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidUpdateEvent
public class EventUpdateRequest {
    private String name;

    private Long categoryId;

    private String description;

    private String address;

    private String location;

    private Double locationLongitude;

    private Double locationLatitude;

}

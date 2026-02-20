package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.response.EventImageResponse;
import com.auhpp.event_management.entity.EventImage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventImageMapper {
    EventImageResponse toEventImageResponse(EventImage eventImage);
}

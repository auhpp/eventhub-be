package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.response.EventSessionBasicResponse;
import com.auhpp.event_management.entity.EventSession;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventSessionBasicMapper {
    EventSessionBasicResponse toEventSessionBasicResponse(EventSession eventSession);
}

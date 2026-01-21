package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.request.EventCreateRequest;
import com.auhpp.event_management.dto.response.EventResponse;
import com.auhpp.event_management.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EventSessionMapper.class, PointMapper.class, CategoryMapper.class,
        UserMapper.class
})
public interface EventMapper {

    @Mapping(target = "thumbnail", ignore = true)
    Event toEvent(EventCreateRequest eventCreateRequest);

    EventResponse toEventResponse(Event event);
}

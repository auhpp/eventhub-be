package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.request.EventCreateRequest;
import com.auhpp.event_management.dto.request.EventUpdateRequest;
import com.auhpp.event_management.dto.response.EventResponse;
import com.auhpp.event_management.entity.Event;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {EventSessionMapper.class, PointMapper.class, CategoryMapper.class,
        UserMapper.class
})
public interface EventMapper {

    @Mapping(target = "thumbnail", ignore = true)
    @Mapping(target = "poster", ignore = true)
    Event toEvent(EventCreateRequest eventCreateRequest);

    EventResponse toEventResponse(Event event);

    @Mapping(target = "thumbnail", ignore = true)
    @Mapping(target = "poster", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEventFromRequest(EventUpdateRequest request, @MappingTarget Event event);

}

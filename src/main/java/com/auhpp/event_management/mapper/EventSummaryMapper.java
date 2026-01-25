package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.response.EventSummaryResponse;
import com.auhpp.event_management.entity.Event;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, PointMapper.class})
public interface EventSummaryMapper {
    EventSummaryResponse toEventSummaryResponse(Event event);

}

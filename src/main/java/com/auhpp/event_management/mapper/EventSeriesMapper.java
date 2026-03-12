package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.request.EventSeriesCreateRequest;
import com.auhpp.event_management.dto.request.EventSeriesUpdateRequest;
import com.auhpp.event_management.dto.response.EventSeriesResponse;
import com.auhpp.event_management.entity.EventSeries;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UserBasicMapper.class})
public interface EventSeriesMapper {
    @Mapping(target = "avatar", ignore = true)
    @Mapping(target = "coverImage", ignore = true)
    EventSeries toEventSeries(EventSeriesCreateRequest request);

    EventSeriesResponse toEventSeriesResponse(EventSeries eventSeries);

    @Mapping(target = "avatar", ignore = true)
    @Mapping(target = "coverImage", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEventSeries(EventSeriesUpdateRequest request, @MappingTarget EventSeries eventSeries);
}

package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.response.EventSeriesFollowerResponse;
import com.auhpp.event_management.entity.EventSeriesFollower;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {EventSeriesMapper.class, UserBasicMapper.class})
public interface EventSeriesFollowerMapper {
    EventSeriesFollowerResponse toEventSeriesFollowerResponse(EventSeriesFollower eventSeriesFollower);
}

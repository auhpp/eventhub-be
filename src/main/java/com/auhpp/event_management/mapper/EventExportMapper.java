package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.response.EventReportExportResponse;
import com.auhpp.event_management.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventExportMapper {

    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "organizerName", source = "appUser.fullName")
    @Mapping(target = "organizerEmail", source = "appUser.email")
    EventReportExportResponse toEventReportExportResponse(Event event);
}

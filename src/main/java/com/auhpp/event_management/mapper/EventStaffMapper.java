package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.response.EventStaffResponse;
import com.auhpp.event_management.entity.EventStaff;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TicketMapper.class})
public interface EventStaffMapper {

    @Mapping(source = "appUser.fullName", target = "fullName")
    @Mapping(source = "appUser.email", target = "email")
    EventStaffResponse toEventStaffResponse(EventStaff eventStaff);
}

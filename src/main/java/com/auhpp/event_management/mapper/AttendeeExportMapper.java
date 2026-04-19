package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.response.AttendeeExportResponse;
import com.auhpp.event_management.entity.Attendee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttendeeExportMapper {
    @Mapping(source = "owner.fullName", target = "ownerName")
    @Mapping(source = "owner.email", target = "ownerEmail")
    @Mapping(source = "owner.phoneNumber", target = "ownerPhone")
    @Mapping(source = "ticket.name", target = "ticketName")
    AttendeeExportResponse toAttendeeExportResponse(Attendee attendee);
}

package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.response.AttendeeBasicResponse;
import com.auhpp.event_management.entity.Attendee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {TicketBasicMapper.class, UserBasicMapper.class})
public interface AttendeeBasicMapper {

    AttendeeBasicResponse toAttendeeBasicResponse(Attendee attendee);
}

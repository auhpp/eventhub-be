package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.request.AttendeeCreateRequest;
import com.auhpp.event_management.dto.response.AttendeeResponse;
import com.auhpp.event_management.entity.Attendee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        TicketMapper.class, EventSessionMapper.class,
        EventSummaryMapper.class, BookingSummaryMapper.class
})
public interface AttendeeMapper {
    Attendee toAttendee(AttendeeCreateRequest attendeeCreateRequest);

    @Mapping(target = "booking", source = "booking")
    @Mapping(target = "eventSession", source = "ticket.eventSession")
    @Mapping(target = "event", source = "ticket.eventSession.event")
    @Mapping(target = "user", source = "booking.appUser")
    AttendeeResponse toAttendeeResponse(Attendee attendee);
}

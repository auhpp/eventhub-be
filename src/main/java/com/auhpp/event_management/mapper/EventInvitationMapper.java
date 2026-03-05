package com.auhpp.event_management.mapper;


import com.auhpp.event_management.dto.response.EventInvitationResponse;
import com.auhpp.event_management.entity.EventInvitation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses =
        {UserBasicMapper.class, BookingBasicMapper.class, TicketBasicMapper.class, EventSessionBasicMapper.class})
public interface EventInvitationMapper {
    @Mapping(source = "ticket.eventSession", target = "eventSession")
    EventInvitationResponse toEventInvitationResponse(EventInvitation eventInvitation);
}

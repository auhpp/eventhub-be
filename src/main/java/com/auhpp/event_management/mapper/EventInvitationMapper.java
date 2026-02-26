package com.auhpp.event_management.mapper;


import com.auhpp.event_management.dto.response.EventInvitationResponse;
import com.auhpp.event_management.entity.EventInvitation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses =
        {UserBasicMapper.class, BookingBasicMapper.class, TicketBasicMapper.class})
public interface EventInvitationMapper {
    EventInvitationResponse toEventInvitationResponse(EventInvitation eventInvitation);
}

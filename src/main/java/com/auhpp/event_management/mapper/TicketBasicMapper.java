package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.response.TicketBasicResponse;
import com.auhpp.event_management.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketBasicMapper {

    @Mapping(source = "eventSession.id", target = "eventSessionId")
    @Mapping(source = "eventSession.event.id", target = "eventId")
    TicketBasicResponse toTicketBasicResponse(Ticket ticket);
}

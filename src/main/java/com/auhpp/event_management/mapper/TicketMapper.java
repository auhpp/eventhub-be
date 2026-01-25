package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.request.TicketCreateRequest;
import com.auhpp.event_management.dto.response.TicketResponse;
import com.auhpp.event_management.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    Ticket toTicket(TicketCreateRequest ticketCreateRequest);

    @Mapping(source = "eventSession.id", target = "eventSessionId")
    TicketResponse toTicketResponse(Ticket ticket);
}

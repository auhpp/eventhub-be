package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.request.TicketCreateRequest;
import com.auhpp.event_management.dto.request.TicketUpdateRequest;
import com.auhpp.event_management.dto.response.TicketResponse;
import com.auhpp.event_management.entity.Ticket;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    Ticket toTicket(TicketCreateRequest ticketCreateRequest);

    @Mapping(source = "eventSession.id", target = "eventSessionId")
    @Mapping(source = "eventSession.event.id", target = "eventId")
    TicketResponse toTicketResponse(Ticket ticket);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTicketFromRequest(TicketUpdateRequest request, @MappingTarget Ticket ticket);
}

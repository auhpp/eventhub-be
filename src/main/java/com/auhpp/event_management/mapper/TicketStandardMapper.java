package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.response.TicketStandardResponse;
import com.auhpp.event_management.entity.Ticket;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {EventSessionBasicMapper.class})
public interface TicketStandardMapper {
    TicketStandardResponse toTicketStandardResponse(Ticket ticket);
}

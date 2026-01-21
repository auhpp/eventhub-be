package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.request.EventSessionCreateRequest;
import com.auhpp.event_management.dto.response.EventSessionResponse;
import com.auhpp.event_management.entity.EventSession;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {TicketMapper.class})
public interface EventSessionMapper {
    EventSession toEventSession(EventSessionCreateRequest eventSessionCreateRequest);

    EventSessionResponse toEventSessionResponse(EventSession eventSession);
}

package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.request.EventSessionCreateRequest;
import com.auhpp.event_management.dto.request.EventSessionUpdateRequest;
import com.auhpp.event_management.dto.response.EventSessionResponse;
import com.auhpp.event_management.entity.EventSession;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {TicketMapper.class})
public interface EventSessionMapper {
    EventSession toEventSession(EventSessionCreateRequest eventSessionCreateRequest);

    EventSessionResponse toEventSessionResponse(EventSession eventSession);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEventSessionFromRequest(EventSessionUpdateRequest request,
                                       @MappingTarget EventSession eventSession);
}

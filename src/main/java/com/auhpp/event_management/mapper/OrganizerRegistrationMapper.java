package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.request.OrganizerCreateRequest;
import com.auhpp.event_management.dto.request.OrganizerUpdateRequest;
import com.auhpp.event_management.dto.response.OrganizerRegistrationResponse;
import com.auhpp.event_management.dto.response.UserResponse;
import com.auhpp.event_management.entity.OrganizerRegistration;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface OrganizerRegistrationMapper {

    @Mapping(target = "businessAvatarUrl", ignore = true)
    OrganizerRegistration toOrganizerRegistration(OrganizerCreateRequest organizerCreateRequest);

    OrganizerRegistrationResponse toOrganizerRegistrationResponse(OrganizerRegistration organizerRegistration);

    @Mapping(target = "businessAvatarUrl", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateOrganizerRegistrationFromRequest(OrganizerUpdateRequest request,
                                                @MappingTarget OrganizerRegistration entity);
}

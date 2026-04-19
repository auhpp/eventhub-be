package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.response.OrganizerRegistrationExcelReportResponse;
import com.auhpp.event_management.entity.OrganizerRegistration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrganizerExportMapper {

    @Mapping(target = "appUserEmail", source = "appUser.email")
    OrganizerRegistrationExcelReportResponse toOrganizerRegistrationExcelReportResponse(OrganizerRegistration organizerRegistration);
}

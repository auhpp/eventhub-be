package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.request.SystemConfigUpdateRequest;
import com.auhpp.event_management.dto.response.SystemConfigurationResponse;
import com.auhpp.event_management.entity.SystemConfiguration;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface SystemConfigurationMapper {
    SystemConfigurationResponse toSystemConfigurationResponse(SystemConfiguration systemConfiguration);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSystemConfigFromRequest(SystemConfigUpdateRequest request,
                                       @MappingTarget SystemConfiguration systemConfiguration);
}

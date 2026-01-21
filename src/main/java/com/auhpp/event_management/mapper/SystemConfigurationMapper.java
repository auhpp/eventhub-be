package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.response.SystemConfigurationResponse;
import com.auhpp.event_management.entity.SystemConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SystemConfigurationMapper {
    SystemConfigurationResponse toSystemConfigurationResponse(SystemConfiguration systemConfiguration);
}

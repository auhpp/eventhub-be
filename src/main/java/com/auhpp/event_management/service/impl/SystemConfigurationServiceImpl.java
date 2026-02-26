package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.SystemConfigurationKey;
import com.auhpp.event_management.dto.request.SystemConfigUpdateRequest;
import com.auhpp.event_management.dto.response.SystemConfigurationResponse;
import com.auhpp.event_management.entity.SystemConfiguration;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.SystemConfigurationMapper;
import com.auhpp.event_management.repository.SystemConfigurationRepository;
import com.auhpp.event_management.service.SystemConfigurationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SystemConfigurationServiceImpl implements SystemConfigurationService {

    SystemConfigurationRepository systemConfigurationRepository;
    SystemConfigurationMapper systemConfigurationMapper;

    @Override
    public SystemConfigurationResponse getConfigByKey(SystemConfigurationKey key) {
        SystemConfiguration systemConfiguration = systemConfigurationRepository.findByKey(key);
        return systemConfigurationMapper.toSystemConfigurationResponse(systemConfiguration);
    }

    @Override
    public SystemConfigurationResponse updateConfig(Long id, SystemConfigUpdateRequest request) {
        SystemConfiguration systemConfiguration = systemConfigurationRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        systemConfigurationMapper.updateSystemConfigFromRequest(request, systemConfiguration);
        systemConfigurationRepository.save(systemConfiguration);
        return systemConfigurationMapper.toSystemConfigurationResponse(systemConfiguration);
    }
}

package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.SystemConfigurationKey;
import com.auhpp.event_management.dto.response.SystemConfigurationResponse;
import com.auhpp.event_management.entity.SystemConfiguration;
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
}

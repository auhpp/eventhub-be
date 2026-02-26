package com.auhpp.event_management.service;

import com.auhpp.event_management.constant.SystemConfigurationKey;
import com.auhpp.event_management.dto.request.SystemConfigUpdateRequest;
import com.auhpp.event_management.dto.response.SystemConfigurationResponse;

public interface SystemConfigurationService {
    SystemConfigurationResponse getConfigByKey(SystemConfigurationKey key);

    SystemConfigurationResponse updateConfig(Long id, SystemConfigUpdateRequest request);
}

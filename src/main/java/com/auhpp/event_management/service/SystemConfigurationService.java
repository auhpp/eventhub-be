package com.auhpp.event_management.service;

import com.auhpp.event_management.constant.SystemConfigurationKey;
import com.auhpp.event_management.dto.response.SystemConfigurationResponse;

public interface SystemConfigurationService {
    SystemConfigurationResponse getConfigByKey(SystemConfigurationKey key);
}

package com.auhpp.event_management.controller;

import com.auhpp.event_management.constant.SystemConfigurationKey;
import com.auhpp.event_management.dto.response.SystemConfigurationResponse;
import com.auhpp.event_management.service.SystemConfigurationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/system-config")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SystemConfigurationController {

    SystemConfigurationService systemConfigurationService;

    @GetMapping("/{configKey}")
    public ResponseEntity<SystemConfigurationResponse> getConfigByKey(
            @PathVariable(name = "configKey") SystemConfigurationKey key
    ) {
        SystemConfigurationResponse result = systemConfigurationService.getConfigByKey(key);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }
}

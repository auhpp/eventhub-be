package com.auhpp.event_management.controller;

import com.auhpp.event_management.constant.SystemConfigurationKey;
import com.auhpp.event_management.dto.request.SystemConfigUpdateRequest;
import com.auhpp.event_management.dto.response.SystemConfigurationResponse;
import com.auhpp.event_management.service.SystemConfigurationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{configId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SystemConfigurationResponse> updateSystemConfig(
            @PathVariable(name = "configId") Long id,
            @Valid @RequestBody SystemConfigUpdateRequest request
    ) {
        SystemConfigurationResponse response = systemConfigurationService.updateConfig(id, request);
        return ResponseEntity
                .status(HttpStatus.OK).body(response);
    }
}

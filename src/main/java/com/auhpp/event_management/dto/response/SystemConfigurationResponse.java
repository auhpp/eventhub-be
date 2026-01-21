package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.SystemConfigurationKey;
import com.auhpp.event_management.constant.SystemConfigurationUnit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemConfigurationResponse {
    private Long id;

    private SystemConfigurationKey key;

    private Double value;

    private String name;

    private String description;

    private SystemConfigurationUnit unit;
}

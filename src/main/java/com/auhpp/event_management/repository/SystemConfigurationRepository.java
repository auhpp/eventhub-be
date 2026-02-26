package com.auhpp.event_management.repository;

import com.auhpp.event_management.constant.SystemConfigurationKey;
import com.auhpp.event_management.entity.SystemConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemConfigurationRepository extends JpaRepository<SystemConfiguration, Long> {
    SystemConfiguration findByKey(SystemConfigurationKey key);

}

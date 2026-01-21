package com.auhpp.event_management.repository;

import com.auhpp.event_management.constant.RoleName;
import com.auhpp.event_management.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleName name);
}

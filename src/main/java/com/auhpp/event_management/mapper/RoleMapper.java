package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.response.RoleResponse;
import com.auhpp.event_management.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleResponse toRoleResponse(Role role);
}
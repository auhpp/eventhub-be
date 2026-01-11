package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.request.RegisterRequest;
import com.auhpp.event_management.entity.AppUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    AppUser toAppUser(RegisterRequest registerRequest);
}

package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.request.RegisterRequest;
import com.auhpp.event_management.dto.request.UserUpdateRequest;
import com.auhpp.event_management.dto.response.UserResponse;
import com.auhpp.event_management.entity.AppUser;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {
    AppUser toAppUser(RegisterRequest registerRequest);

    UserResponse toUserResponse(AppUser appUser);

    @Mapping(target = "avatar", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromRequest(UserUpdateRequest request, @MappingTarget AppUser user);
}

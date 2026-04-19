package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.response.UserBasicResponse;
import com.auhpp.event_management.entity.AppUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {RoleMapper.class, SocialLinkMapper.class})
public interface UserBasicMapper {
    UserBasicResponse toUserBasicResponse(AppUser appUser);
}

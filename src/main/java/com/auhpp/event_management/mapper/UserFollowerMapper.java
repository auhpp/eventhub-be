package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.response.UserFollowerResponse;
import com.auhpp.event_management.entity.UserFollower;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserBasicMapper.class})
public interface UserFollowerMapper {
    UserFollowerResponse toUserFollowerResponse(UserFollower userFollower);
}

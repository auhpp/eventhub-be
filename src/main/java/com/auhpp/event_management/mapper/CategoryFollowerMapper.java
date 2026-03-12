package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.response.CategoryFollowerResponse;
import com.auhpp.event_management.entity.CategoryFollower;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, UserBasicMapper.class})
public interface CategoryFollowerMapper {
    CategoryFollowerResponse toCategoryFollowerResponse(CategoryFollower categoryFollower);
}

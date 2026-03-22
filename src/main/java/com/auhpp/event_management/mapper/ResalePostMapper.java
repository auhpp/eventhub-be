package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.response.ResalePostResponse;
import com.auhpp.event_management.entity.ResalePost;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserBasicMapper.class, AttendeeBasicMapper.class})
public interface ResalePostMapper {
    ResalePostResponse toResalePostResponse(ResalePost resalePost);
}

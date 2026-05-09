package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.response.ReplyMessageResponse;
import com.auhpp.event_management.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserBasicMapper.class})
public interface ReplyMessageMapper {
    @Mapping(source = "sender.appUser", target = "sender")
    ReplyMessageResponse toMessageResponse(Message message);
}

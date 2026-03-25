package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.response.ReplyMessageResponse;
import com.auhpp.event_management.entity.Message;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserBasicMapper.class})
public interface ReplyMessageMapper {
    ReplyMessageResponse toMessageResponse(Message message);
}

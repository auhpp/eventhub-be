package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.response.MessageResponse;
import com.auhpp.event_management.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserBasicMapper.class, ReplyMessageMapper.class})
public interface MessageMapper {
    @Mapping(target = "conversationId", source = "recipient.conversation.id")
    @Mapping(target = "sender", source = "sender.appUser")
    @Mapping(target = "recipient", source = "recipient.appUser")
    MessageResponse toMessageResponse(Message message);


}

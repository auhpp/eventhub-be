package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.response.ConversationMemberResponse;
import com.auhpp.event_management.entity.ConversationMember;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserBasicMapper.class})
public interface ConversationMemberMapper {
    ConversationMemberResponse toConversationMemberResponse(ConversationMember conversationMember);
}

package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.request.ConversationUpdateRequest;
import com.auhpp.event_management.dto.response.ConversationResponse;
import com.auhpp.event_management.entity.Conversation;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {ConversationMemberMapper.class, MessageMapper.class})
public interface ConversationMapper {
    ConversationResponse toConversationResponse(Conversation conversation);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateConversation(ConversationUpdateRequest request, @MappingTarget Conversation conversation);
}

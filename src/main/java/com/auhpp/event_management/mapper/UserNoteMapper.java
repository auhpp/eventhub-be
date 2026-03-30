package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.response.UserNoteResponse;
import com.auhpp.event_management.entity.UserNote;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserNoteMapper {
    UserNoteResponse toUserNoteResponse(UserNote userNote);
}

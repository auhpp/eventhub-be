package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.request.TagCreateRequest;
import com.auhpp.event_management.dto.response.TagResponse;
import com.auhpp.event_management.entity.EventTag;
import com.auhpp.event_management.entity.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagResponse toTagResponse(Tag tag);

    Tag toTag(TagCreateRequest request);

    default TagResponse eventTagToTagResponse(EventTag eventTag) {
        if (eventTag == null || eventTag.getTag() == null) {
            return null;
        }
        return toTagResponse(eventTag.getTag());
    }
}

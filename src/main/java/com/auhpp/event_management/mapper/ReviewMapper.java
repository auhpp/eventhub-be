package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.request.ReviewUpdateRequest;
import com.auhpp.event_management.dto.response.ReviewResponse;
import com.auhpp.event_management.entity.Review;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {ReviewImageMapper.class, UserBasicMapper.class})
public interface ReviewMapper {

    @Mapping(target = "user", source = "attendee.owner")
    @Mapping(target = "fullNameEventStaff", source = "eventStaff.appUser.fullName")
    @Mapping(target = "emailEventStaff", source = "eventStaff.appUser.email")
    @Mapping(target = "avatarEventStaff", source = "eventStaff.appUser.avatar")
    @Mapping(target = "eventName", source = "eventSession.event.name")
    @Mapping(target = "eventId", source = "eventSession.event.id")
    @Mapping(target = "startDateTime", source = "eventSession.startDateTime")
    @Mapping(target = "thumbnailUrl", source = "eventSession.event.thumbnail")
    ReviewResponse toReviewResponse(Review review);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateReview(ReviewUpdateRequest request, @MappingTarget Review review);
}

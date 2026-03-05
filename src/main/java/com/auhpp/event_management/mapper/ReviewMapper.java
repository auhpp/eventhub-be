package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.request.ReviewUpdateRequest;
import com.auhpp.event_management.dto.response.ReviewResponse;
import com.auhpp.event_management.entity.Review;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {ReviewImageMapper.class, UserBasicMapper.class})
public interface ReviewMapper {

    @Mapping(target = "user", source = "attendee.owner")
    ReviewResponse toReviewResponse(Review review);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateReview(ReviewUpdateRequest request, @MappingTarget Review review);
}

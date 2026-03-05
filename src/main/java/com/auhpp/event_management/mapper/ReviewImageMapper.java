package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.response.ReviewImageResponse;
import com.auhpp.event_management.entity.ReviewImage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewImageMapper {
    ReviewImageResponse toReviewImageResponse(ReviewImage reviewImage);
}

package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.request.SocialLinkCreateRequest;
import com.auhpp.event_management.dto.response.SocialLinkResponse;
import com.auhpp.event_management.entity.SocialLink;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface SocialLinkMapper {
    SocialLink toSocialLink(SocialLinkCreateRequest request);

    SocialLinkResponse toSocialLinkResponse(SocialLink socialLink);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSocialLinkFromRequest(SocialLinkCreateRequest request, @MappingTarget SocialLink socialLink);
}

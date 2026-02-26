package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.SocialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SocialLinkResponse {
    private Long id;

    private String urlLink;

    private SocialType type;

}

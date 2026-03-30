package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.TagType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagSearchRequest {
    private TagType type;

    private String name;

    private Long userId;
}

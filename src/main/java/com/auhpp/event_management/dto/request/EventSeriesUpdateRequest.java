package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.EventSeriesStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventSeriesUpdateRequest {

    private MultipartFile avatar;

    private MultipartFile coverImage;

    private String name;

    private String description;

    private String hasPublic;

    private EventSeriesStatus status;
}

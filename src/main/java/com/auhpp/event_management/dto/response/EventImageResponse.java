package com.auhpp.event_management.dto.response;

import com.auhpp.event_management.constant.ProcessStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventImageResponse {
    private Long id;
    private String imageUrl;
    private ProcessStatus processStatus;
}

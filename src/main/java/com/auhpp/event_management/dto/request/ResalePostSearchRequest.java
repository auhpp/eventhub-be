package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.ResalePostStatus;
import com.auhpp.event_management.constant.SortType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResalePostSearchRequest {
    private SortType sortType;
    private Integer quantity;
    private Long eventSessionId;
    private Long ticketId;
    private Boolean hasRetail;
    private Long userId;
    private List<ResalePostStatus> statuses;
    private Long eventId;
    private String name;
    private String email;
}

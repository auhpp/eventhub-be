package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingSearchRequest {
    private BookingStatus status;
    private Long userId;
    private Long eventSessionId;

}

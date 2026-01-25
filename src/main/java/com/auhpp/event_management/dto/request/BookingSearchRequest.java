package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.BookingSearchStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingSearchRequest {
    private BookingSearchStatus status;
}

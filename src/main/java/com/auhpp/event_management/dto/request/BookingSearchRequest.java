package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.BookingStatus;
import com.auhpp.event_management.constant.BookingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingSearchRequest {
    private BookingStatus status;
    private Long userId;
    private Long eventSessionId;
    private Boolean upcoming;
    private Long bookingId;
    private String email;
    private List<BookingType> types;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

}

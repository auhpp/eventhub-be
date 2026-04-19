package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.response.BookingReportResponse;
import com.auhpp.event_management.entity.Booking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingReportMapper {
    BookingReportResponse toBookingReportResponse(Booking booking);
}

package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.response.BookingBasicResponse;
import com.auhpp.event_management.entity.Booking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AttendeeBasicMapper.class})
public interface BookingBasicMapper {
    BookingBasicResponse toBookingBasicResponse(Booking booking);
}

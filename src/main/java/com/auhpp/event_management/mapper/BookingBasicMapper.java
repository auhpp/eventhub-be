package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.response.BookingBasicResponse;
import com.auhpp.event_management.entity.Attendee;
import com.auhpp.event_management.entity.Booking;
import com.auhpp.event_management.entity.Event;
import com.auhpp.event_management.entity.EventSession;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AttendeeBasicMapper.class, EventBasicMapper.class,
        EventSessionBasicMapper.class})
public interface BookingBasicMapper {

    @Mapping(target = "event", source = ".")
    @Mapping(target = "eventSession", source = ".")
    BookingBasicResponse toBookingBasicResponse(Booking booking);

    default Event getEventFromBooking(Booking booking) {
        if (booking == null || booking.getAttendees() == null || booking.getAttendees().isEmpty()) {
            return null;
        }

        Attendee firstAttendee = booking.getAttendees().getFirst();

        if (firstAttendee.getTicket() != null &&
                firstAttendee.getTicket().getEventSession() != null) {
            return firstAttendee.getTicket().getEventSession().getEvent();
        }

        return null;
    }

    default EventSession getEventSessionFromBooking(Booking booking) {
        if (booking == null || booking.getAttendees() == null || booking.getAttendees().isEmpty()) {
            return null;
        }

        Attendee firstAttendee = booking.getAttendees().getFirst();

        if (firstAttendee.getTicket() != null &&
                firstAttendee.getTicket().getEventSession() != null) {
            return firstAttendee.getTicket().getEventSession();
        }

        return null;
    }
}

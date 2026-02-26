package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.request.BookingPaymentRequest;
import com.auhpp.event_management.dto.request.PendingBookingCreateRequest;
import com.auhpp.event_management.dto.response.BookingResponse;
import com.auhpp.event_management.entity.Attendee;
import com.auhpp.event_management.entity.Booking;
import com.auhpp.event_management.entity.Event;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {AttendeeMapper.class,
        UserMapper.class, EventMapper.class})
public interface BookingMapper {
    Booking toPendingBooking(PendingBookingCreateRequest pendingBookingCreateRequest);

    @Mapping(target = "event", source = ".")
    BookingResponse toBookingResponse(Booking booking);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBookingFromRequest(BookingPaymentRequest request,
                                  @MappingTarget Booking entity);

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
}

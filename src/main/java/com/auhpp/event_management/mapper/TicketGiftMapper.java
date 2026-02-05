package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.response.TicketGiftResponse;
import com.auhpp.event_management.entity.TicketGift;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserBasicMapper.class, BookingBasicMapper.class})
public interface TicketGiftMapper {
    TicketGiftResponse toTicketGiftResponse(TicketGift ticketGift);
}

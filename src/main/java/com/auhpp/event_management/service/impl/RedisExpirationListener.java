package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.RedisPrefix;
import com.auhpp.event_management.service.BookingService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class RedisExpirationListener extends KeyExpirationEventMessageListener {

    private final BookingService bookingService;

    public RedisExpirationListener(RedisMessageListenerContainer listenerContainer,
                                   BookingService bookingService) {
        super(listenerContainer);
        this.bookingService = bookingService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        if (expiredKey.startsWith(RedisPrefix.BOOKING_EXPIRATION.getValue())) {
            String orderIdStr = expiredKey.split(":")[1];
            Long orderId = Long.parseLong(orderIdStr);

            // Call method delete booking
            bookingService.deleteBooking(orderId);
        }
    }

}

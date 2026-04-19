package com.auhpp.event_management.service;

import com.auhpp.event_management.entity.Booking;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface InvoiceService {
    CompletableFuture<String> createInvoice(Booking booking) throws IOException;

    String getInvoiceUrl(Long bookingId) throws IOException;
}

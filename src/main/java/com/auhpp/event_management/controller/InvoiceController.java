package com.auhpp.event_management.controller;

import com.auhpp.event_management.service.InvoiceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/invoice")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvoiceController {
    InvoiceService invoiceService;

    @GetMapping("/{bookingId}")
    public ResponseEntity<String> getInvoiceUrl(
            @PathVariable("bookingId") Long bookingId
    ) throws IOException {
        String invoiceUrl = invoiceService.getInvoiceUrl(bookingId);
        return ResponseEntity.status(HttpStatus.OK).body(invoiceUrl);
    }
}

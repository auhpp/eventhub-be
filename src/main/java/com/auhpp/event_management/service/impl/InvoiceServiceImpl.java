package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.FolderName;
import com.auhpp.event_management.dto.response.GroupedTicketResponse;
import com.auhpp.event_management.entity.*;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.repository.BookingRepository;
import com.auhpp.event_management.service.CloudinaryService;
import com.auhpp.event_management.service.InvoiceService;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    BookingRepository bookingRepository;
    SpringTemplateEngine templateEngine;
    CloudinaryService cloudinaryService;

    @Override
    @Async
    public CompletableFuture<String> createInvoice(Booking booking) throws IOException {
        EventSession eventSession = booking.getAttendees().getFirst().getTicket().getEventSession();
        Event event = eventSession.getEvent();
        Map<Long, List<Attendee>> groupedTickets = booking.getAttendees().stream()
                .collect(Collectors.groupingBy(attendee -> attendee.getTicket().getId()));
        List<GroupedTicketResponse> groupedTicketResponses = new ArrayList<>();

        for (Long key : groupedTickets.keySet()) {
            Ticket ticket = groupedTickets.get(key).getFirst().getTicket();
            Attendee attendee = groupedTickets.get(key).getFirst();
            int quantity = groupedTickets.get(key).size();
            groupedTicketResponses.add(GroupedTicketResponse.builder()
                    .name(ticket.getName())
                    .price(attendee.getPrice())
                    .quantity(quantity)
                    .build());
        }

        Context context = new Context();
        context.setVariable("event", event);
        context.setVariable("eventSession", eventSession);
        context.setVariable("booking", booking);
        context.setVariable("tickets", groupedTicketResponses);
        String html = templateEngine.process("invoice-template", context);

        // convert to pdf
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();

        builder.useFastMode();

        // config font
        File fontFile = new ClassPathResource("fonts/Roboto-Regular.ttf").getFile();
        builder.useFont(fontFile, "Arial");

        // content
        builder.withHtmlContent(html, "/");
        builder.toStream(outputStream);
        builder.run();

        byte[] byteFile = outputStream.toByteArray();

        // upload to cloudinary
        String fileName = "hoa-don-" + booking.getId();
        String invoiceUrl = cloudinaryService.uploadPdf(byteFile,
                FolderName.INVOICE.getValue() + booking.getId(), fileName);
        booking.setInvoiceUrl(invoiceUrl);
        bookingRepository.save(booking);
        return CompletableFuture.completedFuture(invoiceUrl);
    }

    @Override
    public String getInvoiceUrl(Long bookingId) throws IOException {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (booking.getInvoiceUrl() == null) {
            return this.createInvoice(booking).join();
        }
        return booking.getInvoiceUrl();
    }
}

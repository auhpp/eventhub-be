package com.auhpp.event_management.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.auhpp.event_management.constant.*;
import com.auhpp.event_management.dto.request.*;
import com.auhpp.event_management.dto.response.*;
import com.auhpp.event_management.entity.*;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.*;
import com.auhpp.event_management.repository.*;
import com.auhpp.event_management.service.AttendeeService;
import com.auhpp.event_management.service.BookingService;
import com.auhpp.event_management.service.InvoiceService;
import com.auhpp.event_management.util.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    BookingRepository bookingRepository;
    BookingMapper bookingMapper;
    TicketRepository ticketRepository;
    RedisTemplate<String, String> stringValueRedisTemplate;
    AttendeeService attendeeService;
    AppUserRepository appUserRepository;
    BookingBasicMapper bookingBasicMapper;
    UserBasicMapper userBasicMapper;
    CouponRepository couponRepository;
    AttendeeRepository attendeeRepository;
    AttendeeBasicMapper attendeeBasicMapper;
    TicketGiftRepository ticketGiftRepository;
    ResalePostRepository resalePostRepository;
    WalletRepository walletRepository;
    InvoiceService invoiceService;
    BookingReportMapper bookingReportMapper;

    private boolean checkValidDateSellTicket(Ticket ticket) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        if (ticket.getOpenAt().isAfter(currentDateTime) || ticket.getEndAt().isBefore(currentDateTime)) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public BookingResponse createPendingBooking(PendingBookingCreateRequest pendingBookingCreateRequest) {
        String email = SecurityUtils.getCurrentUserLogin();
        AppUser appUser = appUserRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        Booking booking = bookingMapper.toPendingBooking(pendingBookingCreateRequest);
        booking.setAppUser(appUser);
        booking.setStatus(BookingStatus.PENDING);
        booking.setExpiredAt(LocalDateTime.now().plusMinutes(5));
        booking.setCustomerEmail(appUser.getEmail());
        bookingRepository.save(booking);

        double totalAmount = 0;
        for (BookingTicketRequest bookingTicketRequest : pendingBookingCreateRequest.getBookingTicketRequests()) {
            // check quantity this ticket type user bought
            int quantityBought = attendeeRepository.countBoughtTicket(bookingTicketRequest.getTicketId(), appUser.getId());
            Ticket ticket = ticketRepository.findById(bookingTicketRequest.getTicketId()).orElseThrow(
                    () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
            );

            if (ticket.getMaximumPerUser() != null) {
                if (quantityBought + bookingTicketRequest.getQuantity() > ticket.getMaximumPerUser()) {
                    throw new AppException(ErrorCode.MAXIMUM_TICKET_PER_USER);
                }
            }

            if (Objects.equals(ticket.getEventSession().getEvent().getAppUser().getId(), appUser.getId())) {
                throw new AppException(ErrorCode.INVALID_TIME_BOOKING);
            }
            int bookingTicketQuantity = bookingTicketRequest.getQuantity();

            if (!checkValidDateSellTicket(ticket)) {
                throw new AppException(ErrorCode.INVALID_TIME_BOOKING);
            }
            if (bookingTicketQuantity > ticket.getMaximumPerPurchase()) {
                throw new AppException(ErrorCode.INVALID_QUANTITY);
            }

            int updatedRows = ticketRepository.reserveTickets(ticket.getId(), bookingTicketRequest.getQuantity());
            if (updatedRows == 0) {
                throw new AppException(ErrorCode.NOT_ENOUGH_QUANTITY);
            }
            totalAmount = totalAmount + bookingTicketQuantity * ticket.getPrice();

            // Create attendee
            for (int i = 0; i < bookingTicketQuantity; i++) {
                attendeeService.createAttendee(AttendeeCreateRequest.builder()
                        .bookingId(booking.getId())
                        .ticketId(ticket.getId())
                        .type(AttendeeType.BUY)
                        .status(AttendeeStatus.INACTIVE)
                        .sourceType(SourceType.PURCHASE)
                        .build());
            }
        }
        booking.setTotalAmount(totalAmount);
        booking.setDiscountAmount(0D);
        booking.setFinalAmount(totalAmount);
        booking.setIsFundReleased(false);
        bookingRepository.save(booking);

        String key = RedisPrefix.BOOKING_EXPIRATION.getValue() + booking.getId();
        stringValueRedisTemplate.opsForValue().set(key, "waiting_payment", 5, TimeUnit.MINUTES);

        return bookingMapper.toBookingResponse(booking);
    }

    @Override
    @Transactional
    public BookingResponse createPendingResaleBooking(PendingResaleBookingCreateRequest request) {
        String email = SecurityUtils.getCurrentUserLogin();
        AppUser appUser = appUserRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );

        ResalePost resalePost = resalePostRepository.findById(request.getResalePostId()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (Objects.equals(resalePost.getAppUser().getId(), appUser.getId())) {
            throw new AppException(ErrorCode.OWNER_CANNOT_BUY);
        }

        Booking booking = new Booking();
        booking.setAppUser(appUser);
        booking.setResalePost(resalePost);
        booking.setStatus(BookingStatus.PENDING);
        booking.setType(BookingType.RESALE);
        booking.setExpiredAt(LocalDateTime.now().plusMinutes(5));
        booking.setCustomerEmail(appUser.getEmail());
        bookingRepository.save(booking);

        double totalAmount = 0;
        for (Long attendeeId : request.getAttendeeIds()) {
            Attendee attendee = attendeeRepository.findById(attendeeId).orElseThrow(
                    () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
            );
            Ticket ticket = attendee.getTicket();
            int quantityBought = attendeeRepository.countBoughtTicket(ticket.getId(), appUser.getId());

            if (ticket.getMaximumPerUser() != null) {
                if (quantityBought + request.getAttendeeIds().size() > ticket.getMaximumPerUser()) {
                    throw new AppException(ErrorCode.MAXIMUM_TICKET_PER_USER);
                }
            }
            // check status
            if (attendee.getStatus() != AttendeeStatus.ON_RESALE) {
                throw new AppException(ErrorCode.ATTENDEE_STATUS_INVALID);
            }
            attendee.setStatus(AttendeeStatus.RESOLD);
            totalAmount += attendee.getPrice();
            attendeeRepository.save(attendee);

            attendeeService.createAttendee(AttendeeCreateRequest.builder()
                    .bookingId(booking.getId())
                    .ticketId(attendee.getTicket().getId())
                    .type(AttendeeType.RESALE)
                    .status(AttendeeStatus.INACTIVE)
                    .sourceType(SourceType.RESALE)
                    .attendeeParentId(attendeeId)
                    .build());
        }
        booking.setTotalAmount(totalAmount);
        booking.setFinalAmount(totalAmount);
        booking.setDiscountAmount(0D);
        booking.setIsFundReleased(false);
        bookingRepository.save(booking);

        String key = RedisPrefix.BOOKING_EXPIRATION.getValue() + booking.getId();
        stringValueRedisTemplate.opsForValue().set(key, "waiting_payment", 5, TimeUnit.MINUTES);

        return bookingMapper.toBookingResponse(booking);
    }

    @Override
    @Transactional
    public Booking createInvitationBooking(InvitationBookingCreateRequest bookingCreateRequest) {
        Booking booking = Booking.builder()
                .totalAmount(0D)
                .discountAmount(0D)
                .finalAmount(0D)
                .status(BookingStatus.PAID)
                .customerEmail(bookingCreateRequest.getUser().getEmail())
                .customerName(bookingCreateRequest.getUser().getFullName())
                .customerPhone(bookingCreateRequest.getUser().getPhoneNumber())
                .type(BookingType.INVITE)
                .appUser(bookingCreateRequest.getUser())
                .createdAt(LocalDateTime.now())
                .build();
        bookingRepository.save(booking);
        for (int i = 0; i < bookingCreateRequest.getQuantity(); i++) {
            attendeeService.createAttendee(
                    AttendeeCreateRequest.builder()
                            .bookingId(booking.getId())
                            .sourceType(SourceType.INVITATION)
                            .owner(bookingCreateRequest.getUser())
                            .ticketId(bookingCreateRequest.getTicketId())
                            .type(AttendeeType.INVITE)
                            .status(AttendeeStatus.VALID)
                            .build()
            );
        }
        return booking;
    }

    @Override
    @Transactional
    public void deleteBooking(Long id) {
        Optional<Booking> bookingOptional = bookingRepository.findById(id);

        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();

            int lockedCount = bookingRepository.lockBookingForDeletion(id);

            if (lockedCount > 0) {
                if (booking.getType() == BookingType.BUY) {
                    Map<Long, Integer> ticketCountMap = new HashMap<>();
                    booking.getAttendees().forEach(
                            attendee -> {
                                Long ticketId = attendee.getTicket().getId();
                                ticketCountMap.put(ticketId, ticketCountMap.getOrDefault(ticketId, 0) + 1);
                            }
                    );
                    ticketCountMap.forEach(ticketRepository::releaseTickets);
                } else if (booking.getType() == BookingType.RESALE) {
                    for (Attendee attendee : booking.getAttendees()) {
                        Attendee attendeeParent = attendee.getParentAttendee();
                        attendeeParent.setStatus(AttendeeStatus.ON_RESALE);
                        attendeeRepository.save(attendeeParent);
                    }
                }

                bookingRepository.delete(booking);

            } else {
                System.out.println("Booking " + id + " đã được xử lý bởi luồng khác. Bỏ qua tác vụ xóa.");
            }
        }
    }

    private void existsBookingExpirationRedisKey(Long id) {
        String key = RedisPrefix.BOOKING_EXPIRATION.getValue() + id;
        boolean isHeld = stringValueRedisTemplate.hasKey(key);
        if (!isHeld) {
            throw new AppException(ErrorCode.INVALID_TIME_BOOKING);
        }
    }

    @Override
    @Transactional
    public BookingResponse updatePaymentInfoBooking(Long id, BookingPaymentRequest bookingPaymentRequest) {
        existsBookingExpirationRedisKey(id);
        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (booking.getStatus() == BookingStatus.PENDING) {
            bookingMapper.updateBookingFromRequest(bookingPaymentRequest, booking);
            if (booking.getType() == BookingType.BUY) {
                booking.getAttendees().forEach(
                        attendee -> {
                            if (!checkValidDateSellTicket(attendee.getTicket())) {
                                deleteBooking(id);
                                throw new AppException(ErrorCode.INVALID_TIME_BOOKING);
                            }
                        }
                );
            }
            return bookingMapper.toBookingResponse(booking);
        } else {
            throw new AppException(ErrorCode.RESOURCE_CAN_NOT_UPDATE);
        }
    }

    @Override
    @Transactional
    public Double calculateFinalAmount(Long id, BookingPaymentRequest request) {
        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        // handle voucher
        if (request.getCouponId() != null) {
            List<Attendee> attendees = booking.getAttendees();
            Coupon coupon = couponRepository.findById(request.getCouponId()).orElseThrow(
                    () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
            );
            // validate coupon
            if (coupon.getBookings().size() == coupon.getMaximumUsage()) {
                throw new AppException(ErrorCode.MAX_COUPON_USAGE);
            }

            // Validate booking
            List<Booking> bookings = couponRepository.findByCouponIdAndUserId(
                    coupon.getId(), booking.getAppUser().getId(), BookingStatus.PAID
            );
            if (bookings.size() == coupon.getMaximumBooking()) {
                throw new AppException(ErrorCode.MAX_BOOKING_PER_USER);
            }
            if (booking.getAttendees().size() < coupon.getMinimumTicketInBooking()) {
                throw new AppException(ErrorCode.MIN_TICKET_IN_BOOKING);
            }
            if (booking.getAttendees().size() > coupon.getMaximumTicketInBooking()) {
                throw new AppException(ErrorCode.MAX_TICKET_IN_BOOKING);
            }
            // attendee valid
            List<Ticket> tickets = coupon.getTicketCoupons().stream()
                    .filter(ticketCoupon -> ticketCoupon.getStatus() == CommonStatus.ACTIVE)
                    .map(TicketCoupon::getTicket).toList();

            List<Attendee> validAttendees = new ArrayList<>();
            for (Attendee attendee : attendees) {
                if (validAttendees.size() == coupon.getMaximumTicketInBooking()) {
                    break;
                }
                Optional<Ticket> foundTicket = tickets.stream().filter(
                                ticket -> Objects.equals(ticket.getId(), attendee.getTicket().getId()))
                        .findAny();
                if (foundTicket.isPresent()) {
                    validAttendees.add(attendee);
                }
            }
            // Handle calculate price
            if (!validAttendees.isEmpty()) {
                double totalValidPrice = validAttendees.stream().mapToDouble(Attendee::getPrice).sum();
                double totalDiscountAmount = 0;
                if (coupon.getDiscountType() == DiscountType.PERCENTAGE) {
                    totalDiscountAmount = totalValidPrice * (coupon.getValue() / 100.0);
                    if (coupon.getMaxDiscountAmount() != null && totalDiscountAmount > coupon.getMaxDiscountAmount()) {
                        totalDiscountAmount = coupon.getMaxDiscountAmount();
                    }
                } else {
                    totalDiscountAmount = coupon.getValue();
                }
                if (totalDiscountAmount > totalValidPrice) {
                    totalDiscountAmount = totalValidPrice;
                }

                // proration
                double remainingDiscount = totalDiscountAmount;
                for (int i = 0; i < validAttendees.size(); i++) {
                    Attendee currentAttendee = validAttendees.get(i);
                    double discountForThisTicket = 0;
                    if (i == validAttendees.size() - 1) {
                        discountForThisTicket = remainingDiscount;
                    } else {
                        double weight = currentAttendee.getPrice() / totalValidPrice;
                        discountForThisTicket = Math.round(weight * totalDiscountAmount);
                    }
                    currentAttendee.setDiscountAmount(discountForThisTicket);
                    currentAttendee.setFinalPrice(currentAttendee.getPrice() - discountForThisTicket);

                    remainingDiscount -= discountForThisTicket;
                }
                booking.setDiscountAmount(totalDiscountAmount);
                booking.setFinalAmount(booking.getTotalAmount() - totalDiscountAmount);
                booking.setCoupon(coupon);
                bookingRepository.save(booking);
            }
        }
        return booking.getFinalAmount();
    }

    @Override
    public BookingResponse getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );

        return bookingMapper.toBookingResponse(booking);
    }

    @Override
    public BookingResponse getBookingByTransactionId(String transactionId, WalletType walletType) {
        Booking booking = bookingRepository.findByTransactionIdAndWalletType(transactionId, walletType).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        return bookingMapper.toBookingResponse(booking);
    }

    @Override
    @Transactional
    public BookingResponse updatePaymentBooking(Long id, LocalDateTime vnpPayDate) throws IOException {
        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );

        if (booking.getStatus() == BookingStatus.PENDING) {
            if (vnpPayDate != null) {
                booking.setCreatedAt(vnpPayDate);
            } else {
                booking.setCreatedAt(LocalDateTime.now());
            }
            existsBookingExpirationRedisKey(id);
            booking.setStatus(BookingStatus.PAID);

            if (booking.getType() == BookingType.BUY || booking.getType() == BookingType.RESALE) {
                WalletType walletType = booking.getType() == BookingType.BUY ? WalletType.ORGANIZER_WALLET :
                        WalletType.USER_WALLET;
                Event event = booking.getAttendees().getFirst().getTicket().getEventSession().getEvent();
                AppUser appUser = booking.getType() == BookingType.BUY ? event.getAppUser() :
                        booking.getResalePost().getAppUser();
                // get wallet
                Wallet wallet = appUser.getWallets().stream().filter(
                        w -> w.getType() == walletType
                ).findFirst().get();

                // update wallet
                double pendingBalance = wallet.getPendingBalance() == null ? 0 : wallet.getPendingBalance();
                wallet.setPendingBalance(pendingBalance + booking.getFinalAmount());
                walletRepository.save(wallet);

            }

            if (booking.getType() == BookingType.RESALE) {
                ResalePost resalePost = booking.getResalePost();
                boolean soldAll = true;
                for (Attendee attendee : resalePost.getAttendees()) {
                    if (attendee.getStatus() != AttendeeStatus.RESOLD) {
                        soldAll = false;
                    }
                }
                if (soldAll) {
                    resalePost.setStatus(ResalePostStatus.SOLD);
                    resalePostRepository.save(resalePost);
                }
                // handle new attendee
                for (Attendee attendee : booking.getAttendees()) {
                    attendeeService.confirmValidAttendee(attendee.getId());
                }
            }
            if (booking.getType() == BookingType.BUY) {
                for (Attendee attendee : booking.getAttendees()) {
                    if (!checkValidDateSellTicket(attendee.getTicket())) {
                        deleteBooking(id);
                        throw new AppException(ErrorCode.INVALID_TIME_BOOKING);
                    }
                    attendeeService.confirmValidAttendee(attendee.getId());
                }
            }
            bookingRepository.save(booking);
        }
        if (booking.getType() == BookingType.BUY || booking.getType() == BookingType.RESALE) {
            invoiceService.createInvoice(booking);
        }
        return bookingMapper.toBookingResponse(booking);
    }

    @Override
    public PageResponse<BookingBasicResponse> getBookings(BookingSearchRequest request,
                                                          int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC,
                "createdAt"));
        Page<Booking> bookings = bookingRepository.filterAll(
                request.getBookingId(),
                request.getEmail(),
                request.getUserId(),
                request.getEventSessionId(),
                request.getStatus(),
                request.getUpcoming(),
                request.getTypes(),
                request.getStartDateTime(),
                request.getEndDateTime(),
                pageable);
        List<BookingBasicResponse> bookingResponse = bookings.getContent().stream().map(
                bookingBasicMapper::toBookingBasicResponse
        ).toList();
        return PageResponse.<BookingBasicResponse>builder()
                .currentPage(page)
                .totalElements(bookings.getTotalElements())
                .totalPage(bookings.getTotalPages())
                .pageSize(bookings.getSize())
                .data(bookingResponse)
                .build();
    }

    @Override
    @Scheduled(fixedRate = 60000, initialDelay = 5000) // 60000 ms = 1 minute, run after 5s server first init
    @Transactional
    public void cleanupExpiredBookings() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<Booking> expiredBookings = bookingRepository.findAllByStatusAndExpiredAtBefore(
                BookingStatus.PENDING, currentDateTime
        );
        if (expiredBookings.isEmpty()) {
            return;
        }
        for (Booking booking : expiredBookings) {
            deleteBooking(booking.getId());
        }
    }

    @Override
    public BookingResponse getBookingByEventSessionIdAndCurrentUserAndStatus(Long eventSessionId, BookingStatus status) {
        String email = SecurityUtils.getCurrentUserLogin();
        Optional<Booking> booking = bookingRepository.findByEventSessionIdAndCurrentUserAndStatus(
                eventSessionId, email, status
        );
        return booking.map(bookingMapper::toBookingResponse).orElse(null);
    }

    @Override
    public BookingResponse getBookingByResalePostIdAndCurrentUserAndStatus(Long resalePostId, BookingStatus status) {
        String email = SecurityUtils.getCurrentUserLogin();
        Optional<Booking> booking = bookingRepository.findByResalePostIdAndCurrentUserAndStatus(
                resalePostId, email, status
        );
        return booking.map(bookingMapper::toBookingResponse).orElse(null);
    }


    @Override
    public UserBookingSummaryResponse getUserBookingSummary(Long eventSessionId, Long userId) {
        AppUser user = appUserRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        List<BookingBasicResponse> responses = new ArrayList<>();

        // Handle gift attendee
        List<TicketGift> ticketGifts = ticketGiftRepository.findAllByUserInAndEventSession(
                TicketGiftStatus.ACCEPTED, List.of(user), eventSessionId
        );
        if (!ticketGifts.isEmpty()) {
            Map<Long, List<TicketGift>> mapGiverId = ticketGifts.stream().collect(
                    Collectors.groupingBy(t -> t.getSender().getId())
            );
            Map<Long, Boolean> checkValidGiver = new HashMap<>();
            for (TicketGift ticketGift : ticketGifts) {
                Long senderId = ticketGift.getSender().getId();
                if (!checkValidGiver.getOrDefault(senderId, false)) {
                    List<Attendee> giftAttendees = mapGiverId.get(senderId).stream().flatMap(
                                    tg -> tg.getAttendeeTicketGifts().stream()
                            ).toList().stream()
                            .map(AttendeeTicketGift::getAttendee).toList();
                    BookingBasicResponse giftBooking = BookingBasicResponse.builder()
                            .sourceType(SourceType.GIFT)
                            .giver(userBasicMapper.toUserBasicResponse(ticketGift.getSender()))
                            .attendees(giftAttendees.stream().map(attendeeBasicMapper::toAttendeeBasicResponse).toList())
                            .build();
                    responses.add(giftBooking);
                    checkValidGiver.put(senderId, true);
                }
            }
        }

        // Handle invitation attendee
        List<Attendee> invitationAttendees = attendeeRepository.findAllByUserInAndEventSession(null, null,
                SourceType.INVITATION, List.of(user), eventSessionId
        );
        if (!invitationAttendees.isEmpty()) {
            BookingBasicResponse invitedBooking = BookingBasicResponse.builder()
                    .sourceType(SourceType.INVITATION)
                    .attendees(invitationAttendees.stream().map(attendeeBasicMapper::toAttendeeBasicResponse).toList())
                    .build();
            responses.add(invitedBooking);
        }

        // handle purchase booking
        List<Booking> bookings = bookingRepository.findAllByUserInAndEventSession(
                List.of(BookingType.BUY, BookingType.RESALE),
                List.of(user), eventSessionId);
        if (!bookings.isEmpty()) {
            for (Booking booking : bookings) {
                BookingBasicResponse basicResponse = bookingBasicMapper.toBookingBasicResponse(booking);
                basicResponse.setSourceType(SourceType.PURCHASE);
                responses.add(basicResponse);
            }
        }

        return UserBookingSummaryResponse
                .builder()
                .user(userBasicMapper.toUserBasicResponse(user))
                .bookings(responses)
                .build();
    }

    @Override
    public void exportReportBookings(ExcelWriter excelWriter, BookingSearchRequest request, String eventName) {
        WriteSheet writeSheet = EasyExcel.writerSheet("danh_sach_don_hang")
                .relativeHeadRowIndex(1)
                .registerWriteHandler(new EventTitleWriteHandler(eventName))
                .build();
        int currentPage = 1;
        int pageSize = 1000;
        boolean hasNextPage = true;
        boolean hasData = false;

        while (hasNextPage) {
            Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.ASC,
                    "createdAt"));
            Page<Booking> pageData = bookingRepository.filterAll(
                    request.getBookingId(),
                    request.getEmail(),
                    request.getUserId(),
                    request.getEventSessionId(),
                    request.getStatus(),
                    request.getUpcoming(),
                    request.getTypes(),
                    request.getStartDateTime(),
                    request.getEndDateTime(),
                    pageable);
            if (pageData == null || pageData.isEmpty()) {
                break;
            }
            hasData = true;
            List<BookingReportResponse> bookingReports = pageData.stream().map(
                    booking -> {
                        BookingReportResponse bookingReport = bookingReportMapper.toBookingReportResponse(booking);

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
                        bookingReport.setCreatedAt(booking.getCreatedAt().format(formatter));

                        bookingReport.setDiscountAmount(booking.getDiscountAmount() == null ? 0
                                : bookingReport.getDiscountAmount());

                        bookingReport.setType(booking.getType() == BookingType.BUY ? "Mua thường" : "Mua lại");

                        bookingReport.setTicketQuantity(booking.getAttendees().size());

                        bookingReport.setStatus(booking.getStatus() == BookingStatus.PAID ? "Đã thanh toán" : "Chưa thanh toán");

                        bookingReport.setWalletType(booking.getWalletType().name());

                        if (booking.getType() == BookingType.RESALE) {
                            bookingReport.setSellBookingId(booking.getAttendees().getFirst().getParentAttendee().getBooking().getId());
                        }
                        return bookingReport;
                    }
            ).collect(Collectors.toList());

            excelWriter.write(bookingReports, writeSheet);

            if (currentPage >= pageData.getTotalPages()) {
                hasNextPage = false;
            } else {
                currentPage++;
            }
        }
        if (!hasData) {
            excelWriter.write(new ArrayList<BookingReportResponse>(), writeSheet);
        }
        excelWriter.finish();
    }


}

package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.*;
import com.auhpp.event_management.dto.request.*;
import com.auhpp.event_management.dto.response.BookingResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.UserBookingSummaryResponse;
import com.auhpp.event_management.entity.AppUser;
import com.auhpp.event_management.entity.Attendee;
import com.auhpp.event_management.entity.Booking;
import com.auhpp.event_management.entity.Ticket;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.BookingBasicMapper;
import com.auhpp.event_management.mapper.BookingMapper;
import com.auhpp.event_management.mapper.UserBasicMapper;
import com.auhpp.event_management.mapper.UserMapper;
import com.auhpp.event_management.repository.AppUserRepository;
import com.auhpp.event_management.repository.BookingRepository;
import com.auhpp.event_management.repository.TicketRepository;
import com.auhpp.event_management.service.AttendeeService;
import com.auhpp.event_management.service.BookingService;
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

import java.time.LocalDateTime;
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
    UserMapper userMapper;
    BookingBasicMapper bookingBasicMapper;
    UserBasicMapper userBasicMapper;

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
            Ticket ticket = ticketRepository.findById(bookingTicketRequest.getTicketId()).orElseThrow(
                    () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
            );
            if (Objects.equals(ticket.getEventSession().getEvent().getAppUser().getId(), appUser.getId())) {
                throw new AppException(ErrorCode.INVALID_TIME_BOOKING);
            }
            int soldQuantity = ticket.getSoldQuantity() == null ? 0 : ticket.getSoldQuantity();
            int bookingTicketQuantity = bookingTicketRequest.getQuantity();
            if (!checkValidDateSellTicket(ticket)) {
                throw new AppException(ErrorCode.INVALID_TIME_BOOKING);
            }
            if (bookingTicketQuantity > ticket.getMaximumPerPurchase()) {
                throw new AppException(ErrorCode.INVALID_QUANTITY);
            }
            if (bookingTicketQuantity > (ticket.getQuantity() - soldQuantity)) {
                throw new AppException(ErrorCode.NOT_ENOUGH_QUANTITY);
            }
            totalAmount = totalAmount + bookingTicketQuantity * ticket.getPrice();
            ticket.setSoldQuantity(soldQuantity + bookingTicketRequest.getQuantity());
            ticketRepository.save(ticket);
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
                .type(AttendeeType.INVITE)
                .appUser(bookingCreateRequest.getUser())
                .build();
        bookingRepository.save(booking);
        for (int i = 0; i < bookingCreateRequest.getQuantity(); i++) {
            attendeeService.createAttendee(
                    AttendeeCreateRequest.builder()
                            .bookingId(booking.getId())
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
            if (booking.getStatus() == BookingStatus.PENDING) {
                Map<Long, Integer> ticketMap = new HashMap<>();
                booking.getAttendees().forEach(
                        attendee -> {
                            Ticket ticket = attendee.getTicket();
                            if (!ticketMap.containsKey(ticket.getId())) {
                                ticketMap.put(ticket.getId(), ticket.getSoldQuantity());
                            }
                            ticketMap.put(ticket.getId(), ticketMap.get(ticket.getId()) - 1);
                        }
                );
                ticketMap.forEach(
                        ticketRepository::updateSoldQuantity
                );
                bookingRepository.deleteById(id);
            } else {
                throw new AppException(ErrorCode.RESOURCE_CAN_NOT_DELETE);
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
            booking.getAttendees().forEach(
                    attendee -> {
                        if (!checkValidDateSellTicket(attendee.getTicket())) {
                            deleteBooking(id);
                            throw new AppException(ErrorCode.INVALID_TIME_BOOKING);
                        }
                    }
            );
            return bookingMapper.toBookingResponse(booking);
        } else {
            throw new AppException(ErrorCode.RESOURCE_CAN_NOT_UPDATE);
        }
    }

    @Override
    public Double calculateFinalAmount(Long id, BookingPaymentRequest bookingPaymentRequest) {
        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        // handle voucher
        return booking.getTotalAmount();
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
    public BookingResponse updatePaymentBooking(Long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (booking.getStatus() == BookingStatus.PENDING) {
            existsBookingExpirationRedisKey(id);
            booking.setStatus(BookingStatus.PAID);
            booking.setCreatedAt(LocalDateTime.now());
            for (Attendee attendee : booking.getAttendees()) {
                if (!checkValidDateSellTicket(attendee.getTicket())) {
                    deleteBooking(id);
                    throw new AppException(ErrorCode.INVALID_TIME_BOOKING);
                }
                attendeeService.confirmValidAttendee(attendee.getId());
            }
            bookingRepository.save(booking);
        }
        return bookingMapper.toBookingResponse(booking);
    }

    @Override
    public PageResponse<BookingResponse> getBookingsByCurrentUser(BookingSearchRequest bookingSearchRequest,
                                                                  int page, int size) {
        String email = SecurityUtils.getCurrentUserLogin();
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC,
                "createdAt"));
        Page<Booking> bookings;
        if (bookingSearchRequest.getStatus() == BookingStatus.PAID) {
            bookings = bookingRepository.findAllByEmailUserAndStatus(email, BookingStatus.PAID, pageable);
        } else if (bookingSearchRequest.getStatus() == BookingStatus.CANCELLED) {
            bookings = bookingRepository.findAllByEmailUserAndStatus(email, BookingStatus.CANCELLED, pageable);
        } else {
            bookings = bookingRepository.findAllByEmailUser(email, pageable);
        }
        List<BookingResponse> bookingResponse = bookings.getContent().stream().map(
                bookingMapper::toBookingResponse
        ).toList();
        return PageResponse.<BookingResponse>builder()
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
    public PageResponse<UserBookingSummaryResponse>
    getUserBookingSummaries(Long eventSessionId,
                            BookingSearchRequest bookingSearchRequest, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC,
                "b.createdAt"));
        List<BookingStatus> bookingStatuses = new ArrayList<>();
        if (bookingSearchRequest.getStatus() == BookingStatus.CANCELLED) {
            bookingStatuses.add(BookingStatus.CANCELLED);
        } else if (bookingSearchRequest.getStatus() == BookingStatus.PAID) {
            bookingStatuses.add(BookingStatus.PAID);
        } else {
            bookingStatuses.addAll(List.of(BookingStatus.CANCELLED, BookingStatus.PAID));
        }
        Page<AppUser> appUserPage = bookingRepository.findUserByEventSession(
                bookingStatuses,
                eventSessionId,
                pageable
        );
        List<UserBookingSummaryResponse> responses = new ArrayList<>();
        if (!appUserPage.isEmpty()) {
            List<AppUser> usersOnPage = appUserPage.getContent();

            List<Booking> bookings = bookingRepository.findAllByUserInAndEventSession(usersOnPage,
                    eventSessionId);
            Map<Long, List<Booking>> bookingsByUserId = bookings.stream().collect(
                    Collectors.groupingBy(b -> b.getAppUser().getId())
            );
            responses = usersOnPage.stream().map(
                    appUser -> {
                        List<Booking> userBookings = bookingsByUserId.getOrDefault(appUser.getId(),
                                Collections.emptyList());
                        return UserBookingSummaryResponse.builder()
                                .user(userBasicMapper.toUserBasicResponse(appUser))
                                .bookings(userBookings.stream().map(bookingBasicMapper::toBookingBasicResponse)
                                        .toList())
                                .build();
                    }
            ).toList();
        }
        return PageResponse.<UserBookingSummaryResponse>builder()
                .currentPage(page)
                .totalElements(appUserPage.getTotalElements())
                .totalPage(appUserPage.getTotalPages())
                .pageSize(appUserPage.getSize())
                .data(responses)
                .build();
    }

    @Override
    public UserBookingSummaryResponse getUserBookingSummary(Long eventSessionId, Long userId) {
        AppUser user = appUserRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        List<Booking> bookings = bookingRepository.findAllByUserInAndEventSession(List.of(user), eventSessionId);
        if (!bookings.isEmpty()) {
            return UserBookingSummaryResponse
                    .builder()
                    .user(userBasicMapper.toUserBasicResponse(user))
                    .bookings(bookings.stream().map(bookingBasicMapper::toBookingBasicResponse).toList())
                    .build();
        }
        return null;
    }


}

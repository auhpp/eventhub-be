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
import com.auhpp.event_management.util.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AttendeeServiceImpl implements AttendeeService {
    AttendeeRepository attendeeRepository;
    BookingRepository bookingRepository;
    TicketRepository ticketRepository;
    AttendeeMapper attendeeMapper;
    AppUserRepository appUserRepository;
    AttendeeBasicMapper attendeeBasicMapper;
    UserBasicMapper userBasicMapper;
    CheckInLogRepository checkInLogRepository;
    EventStaffRepository eventStaffRepository;
    CheckInLogMapper checkInLogMapper;
    AttendeeExportMapper attendeeExportMapper;

    static String CHAR_LOWER = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    static SecureRandom random = new SecureRandom();

    public String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int rndCharAt = random.nextInt(CHAR_LOWER.length());
            char rndChar = CHAR_LOWER.charAt(rndCharAt);
            sb.append(rndChar);
        }
        return sb.toString();
    }

    @Override
    public String generateTicketCode() {
        String ticketCode = "";
        boolean isUnique = false;
        do {
            ticketCode = generateRandomString(8);
            if (attendeeRepository.findByTicketCode(ticketCode).isEmpty()) {
                isUnique = true;
            }
        } while (!isUnique);
        return ticketCode;
    }

    @Override
    @Transactional
    public AttendeeBasicResponse checkIn(CheckInRequest request) {
        Attendee attendee = null;
        if (request.getAttendeeId() == null) {
            attendee = attendeeRepository.findByTicketCode(request.getTicketCode()).orElseThrow(
                    () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
            );
        } else {
            attendee = attendeeRepository.findById(request.getAttendeeId()).orElseThrow(
                    () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
            );
        }

        // common validations
        if (attendee.getStatus() != AttendeeStatus.CHECKED_IN && attendee.getStatus() != AttendeeStatus.OUTSIDE
                && attendee.getStatus() != AttendeeStatus.VALID) {
            throw new AppException(ErrorCode.INVALID_TICKET);
        }

        EventSession eventSession = attendee.getTicket().getEventSession();
        // time checkin
        if (LocalDateTime.now().

                isBefore(eventSession.getCheckinStartTime())) {
            throw new AppException(ErrorCode.CHECK_IN_START_TIME);
        }

        if (eventSession.isExpired()) {
            throw new AppException(ErrorCode.EXPIRED_EVENT_SESSION);
        }

        String email = SecurityUtils.getCurrentUserLogin();
        Event event = eventSession.getEvent();

        // find event staff
        Optional<EventStaff> eventStaff = eventStaffRepository.findByUserEmailAndEventId(email, event.getId());

        if (eventStaff.isEmpty() || !Objects.equals(event.getId(), request.getEventId())) {
            throw new AppException(ErrorCode.WRONG_EVENT);
        }

        // In
        if (request.getActionType() == ActionType.IN) {
            if (attendee.getStatus() == AttendeeStatus.CHECKED_IN) {
                throw new AppException(ErrorCode.CHECKED_IN_TICKET);
            }

            if (attendee.getStatus() == AttendeeStatus.VALID || attendee.getStatus() == AttendeeStatus.OUTSIDE) {
                attendee.setStatus(AttendeeStatus.CHECKED_IN);

                if (attendee.getCheckInAt() == null) {
                    attendee.setCheckInAt(LocalDateTime.now());
                }
                // create checkin log
                attendee.getCheckInLogs().add(CheckInLog.builder()
                        .attendee(attendee)
                        .actionType(ActionType.IN)
                        .eventStaff(eventStaff.get())
                        .build());
            }
        } else {
            // OUT
            if (attendee.getStatus() == AttendeeStatus.VALID) {
                throw new AppException(ErrorCode.NOT_CHECK_IN);
            } else if (attendee.getStatus() == AttendeeStatus.OUTSIDE) {
                throw new AppException(ErrorCode.ATTENDEE_OUTSIDE);
            }

            // update attendee
            attendee.setStatus(AttendeeStatus.OUTSIDE);

            // create checkin log

            attendee.getCheckInLogs().add(CheckInLog.builder()
                    .attendee(attendee)
                    .actionType(ActionType.OUT)
                    .eventStaff(eventStaff.get())
                    .build());
        }

        attendeeRepository.save(attendee);
        return attendeeBasicMapper.toAttendeeBasicResponse(attendee);
    }

    @Transactional
    public AttendeeBasicResponse cancelAttendee(Long id) {
        Attendee attendee = attendeeRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        String email = SecurityUtils.getCurrentUserLogin();
        if (!Objects.equals(email, attendee.getOwner().getEmail())) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        Ticket ticket = attendee.getTicket();

        ticket.setSoldQuantity(ticket.getSoldQuantity() - 1);
        ticketRepository.save(ticket);

        attendee.setStatus(AttendeeStatus.CANCELLED_BY_USER);
        attendeeRepository.save(attendee);
        return attendeeBasicMapper.toAttendeeBasicResponse(attendee);
    }

    @Override
    @Transactional
    public AttendeeResponse createAttendee(AttendeeCreateRequest request) {
        Attendee attendee = attendeeMapper.toAttendee(request);

        Booking booking = bookingRepository.findById(request.getBookingId()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );

        Ticket ticket = ticketRepository.findById(request.getTicketId()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (request.getType() == AttendeeType.INVITE) {
            attendee.setPrice(0D);
            attendee.setFinalPrice(0D);
        } else {
            attendee.setPrice(ticket.getPrice());
            attendee.setFinalPrice(ticket.getPrice());
        }
        attendee.setDiscountAmount(0D);

        attendee.setBooking(booking);
        attendee.setTicket(ticket);
        if (request.getAttendeeParentId() != null) {
            Attendee attendeeParent = attendeeRepository.findById(request.getAttendeeParentId())
                    .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
            attendee.setParentAttendee(attendeeParent);
        }
        Event event = attendee.getTicket().getEventSession().getEvent();
        if (event.getType() == EventType.OFFLINE) {
            attendee.setTicketCode(generateTicketCode());
        }
        attendeeRepository.save(attendee);
        return attendeeMapper.toAttendeeResponse(attendee);
    }

    @Override
    public AttendeeResponse confirmValidAttendee(Long attendeeId) {
        Attendee attendee = attendeeRepository.findById(attendeeId).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        String email = SecurityUtils.getCurrentUserLogin();
        AppUser user = appUserRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        attendee.setOwner(user);
        attendee.setStatus(AttendeeStatus.VALID);
        attendee.setCreatedAt(LocalDateTime.now());
        attendeeRepository.save(attendee);
        return attendeeMapper.toAttendeeResponse(attendee);
    }

    @Override
    public PageResponse<AttendeeResponse> getAttendeesByCurrentUser(AttendeeSearchRequest attendeeSearchRequest,
                                                                    int page, int size) {
        String email = SecurityUtils.getCurrentUserLogin();

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC,
                "createdAt"));
        Page<Attendee> attendees;
        if (attendeeSearchRequest.getSearchStatus() == AttendeeSearchStatus.COMING) {
            attendees = attendeeRepository.findComingAllByEmailUser(LocalDateTime.now(), email, pageable);
        } else if (attendeeSearchRequest.getSearchStatus() == AttendeeSearchStatus.PAST) {
            attendees = attendeeRepository.findPastAllByEmailUser(LocalDateTime.now(), email, pageable);
        } else if (attendeeSearchRequest.getSearchStatus() == AttendeeSearchStatus.CANCELLED) {
            attendees = attendeeRepository.findAllByStatusAndEmailUser(
                    List.of(AttendeeStatus.CANCELLED_BY_USER, AttendeeStatus.CANCELLED_BY_EVENT), email, pageable);
        } else if (attendeeSearchRequest.getSearchStatus() == AttendeeSearchStatus.CHECKED_IN) {
            attendees = attendeeRepository.findAllByStatusAndEmailUser(
                    List.of(AttendeeStatus.CHECKED_IN), email, pageable);
        } else {
            attendees = attendeeRepository.findAllByStatusAndEmailUser(
                    List.of(AttendeeStatus.VALID), email, pageable);
        }
        List<AttendeeResponse> attendeeResponses = attendees.getContent().stream().map(
                attendeeMapper::toAttendeeResponse
        ).toList();
        return PageResponse.<AttendeeResponse>builder()
                .currentPage(page)
                .totalElements(attendees.getTotalElements())
                .totalPage(attendees.getTotalPages())
                .pageSize(attendees.getSize())
                .data(attendeeResponses)
                .build();
    }

    @Override
    public AttendeeResponse getAttendeeById(Long id) {
        Attendee attendee = attendeeRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        return attendeeMapper.toAttendeeResponse(attendee);
    }

    @Override
    public AttendeeResponse assignAttendeeEmail(Long id, String email) {
        Attendee attendee = attendeeRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        String emailAuth = SecurityUtils.getCurrentUserLogin();
        if (!Objects.equals(attendee.getBooking().getAppUser().getEmail(), emailAuth)) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        AppUser user = appUserRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        attendee.setOwner(user);
        attendeeRepository.save(attendee);
        return attendeeMapper.toAttendeeResponse(attendee);
    }

    @Override
    public String getMeetingLink(Long attendeeId) {
        Attendee attendee = attendeeRepository.findById(attendeeId).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (attendee.getStatus() == AttendeeStatus.VALID) {
            String email = SecurityUtils.getCurrentUserLogin();
            if (!Objects.equals(email, attendee.getOwner().getEmail())) {
                throw new AppException(ErrorCode.FORBIDDEN);
            }
            EventSession eventSession = attendee.getTicket().getEventSession();
            if (eventSession.getCheckinStartTime() != null && eventSession.getCheckinStartTime().isBefore(LocalDateTime.now())) {
                throw new AppException(ErrorCode.INVALID_TIME_JOIN);
            }
            return attendee.getTicket().getEventSession().getMeetingUrl();
        }
        return "";
    }

    @Override
    public PageResponse<AttendeeResponse> getAttendees(CheckinSearchRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC,
                "createdAt"));
        Page<Attendee> attendeePage;
        if (request.getStatus() != null && request.getEventSessionId() != null) {
            attendeePage = attendeeRepository.findAllByEventSessionIdAndStatus(request.getEventSessionId(),
                    request.getStatus(), pageable);
        } else if (request.getEventSessionId() != null) {
            attendeePage = attendeeRepository.findAllByEventSessionId(request.getEventSessionId(), pageable);
        } else {
            attendeePage = attendeeRepository.findAll(pageable);
        }
        List<AttendeeResponse> attendeeResponses = attendeePage.getContent().stream().map(
                attendeeMapper::toAttendeeResponse
        ).toList();
        return PageResponse.<AttendeeResponse>builder()
                .currentPage(page)
                .totalElements(attendeePage.getTotalElements())
                .totalPage(attendeePage.getTotalPages())
                .pageSize(attendeePage.getSize())
                .data(attendeeResponses)
                .build();
    }

    @Override
    public PageResponse<UserAttendeeSummaryResponse>
    getUserAttendeeSummaries(Long eventSessionId,
                             AttendeeSearchRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.ASC, "email");
        Page<AppUser> appUserPage = attendeeRepository.findUserByEventSession(
                request.getTicketId(),
                request.getName(),
                request.getEmail(),
                request.getTypes(),
                request.getStatuses(),
                eventSessionId,
                pageable
        );

        List<UserAttendeeSummaryResponse> responses = new ArrayList<>();
        if (!appUserPage.isEmpty()) {
            List<AppUser> usersOnPage = appUserPage.getContent();

            List<Attendee> attendees = attendeeRepository.findAllByUserInAndEventSession(
                    request.getTicketId(),
                    request.getTypes(),
                    null, usersOnPage,
                    eventSessionId);
            Map<Long, List<Attendee>> attendeesByUserId = attendees.stream().collect(
                    Collectors.groupingBy(a -> a.getOwner().getId())
            );
            responses = usersOnPage.stream().map(
                    appUser -> {
                        List<Attendee> userAttendees = attendeesByUserId.getOrDefault(appUser.getId(),
                                Collections.emptyList());
                        return UserAttendeeSummaryResponse.builder()
                                .user(userBasicMapper.toUserBasicResponse(appUser))
                                .attendees(userAttendees.stream().map(attendeeBasicMapper::toAttendeeBasicResponse)
                                        .toList())
                                .build();
                    }
            ).toList();
        }
        return PageResponse.<UserAttendeeSummaryResponse>builder()
                .currentPage(page)
                .totalElements(appUserPage.getTotalElements())
                .totalPage(appUserPage.getTotalPages())
                .pageSize(appUserPage.getSize())
                .data(responses)
                .build();
    }

    @Override
    public String getTicketCode(Long id) {
        Attendee attendee = attendeeRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        String email = SecurityUtils.getCurrentUserLogin();
        if (!Objects.equals(attendee.getOwner().getEmail(), email)) {
            throw new AppException(ErrorCode.ATTENDEE_OWNER_INVALID);
        }
        return attendee.getTicketCode();
    }

    @Override
    public boolean getAttendeeByEventSessionIdAndCurrentUser(Long eventSessionId, Long userId) {
        return attendeeRepository.existsByAppUserIdAndEventSessionId(userId, eventSessionId);
    }

    @Override
    @Transactional
    public AttendanceImportCheckInResponse processAttendanceFromEmails(Long eventSessionId, AttendanceImportRequest request) {
        List<String> cleanEmails = request.getEmails().stream().filter(
                        e -> e != null && !e.isBlank()
                )
                .map(String::trim)
                .map(String::toLowerCase)
                .toList();
        if (cleanEmails.isEmpty()) {
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND);
        }
        List<Attendee> validAttendee = attendeeRepository.findBySessionIdAndEmailsIn(eventSessionId, request.getEmails());
        LocalDateTime now = LocalDateTime.now();
        for (Attendee attendee : validAttendee) {
            attendee.setStatus(AttendeeStatus.CHECKED_IN);
            attendee.setCheckInAt(now);
        }
        attendeeRepository.saveAll(validAttendee);

        List<String> successfulEmails = validAttendee.stream()
                .map(a -> a.getOwner().getEmail()).distinct().toList();
        List<String> failedEmails = cleanEmails.stream().filter(email -> !successfulEmails.contains(email))
                .toList();
        return AttendanceImportCheckInResponse.builder()
                .successCount(validAttendee.size())
                .failedCount(failedEmails.size())
                .failedEmails(failedEmails)
                .build();
    }


    @Override
    public PageResponse<AttendeeCheckinResponse> getAttendeeCheckins(Long ticketId, String email, int page, int size) {
        if (Objects.equals(email, "")) {
            email = null;
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<AttendeeCheckedIn> pageData = attendeeRepository.findAllCheckedInByTicketId(ticketId, email, pageable);
        List<AttendeeCheckinResponse> responses = pageData.getContent().stream().map(
                attendeeCheckedIn -> AttendeeCheckinResponse.builder()
                        .checkedInCount(attendeeCheckedIn.getCheckedInCount())
                        .fullName(attendeeCheckedIn.getFullName())
                        .email(attendeeCheckedIn.getEmail())
                        .userId(attendeeCheckedIn.getUserId()).build()
        ).toList();
        return PageResponse.<AttendeeCheckinResponse>builder()
                .currentPage(page)
                .totalElements(pageData.getTotalElements())
                .totalPage(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .data(responses)
                .build();
    }

    @Override
    public PageResponse<CheckInLogResponse> getCheckInLogs(CheckInLogSearchRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC,
                "createdAt"));
        Page<CheckInLog> pageData = checkInLogRepository.filter(
                request.getTicketCode(), request.getAttendeeId(), request.getActionType(), request.getEventStaffId(),
                request.getFromTime(), request.getToTime(), pageable
        );
        List<CheckInLogResponse> responses = pageData.getContent().stream().map(
                checkInLogMapper::toCheckInLogResponse
        ).toList();
        return PageResponse.<CheckInLogResponse>builder()
                .currentPage(page)
                .totalElements(pageData.getTotalElements())
                .totalPage(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .data(responses)
                .build();
    }

    @Override
    public void exportReportAttendees(ExcelWriter excelWriter, AttendeeSearchRequest request, String eventName) {
        WriteSheet writeSheet = EasyExcel.writerSheet("danh_sach_nguoi_tham_gia")
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
            Page<Attendee> pageData = attendeeRepository.filterReport(
                    request.getName(),
                    request.getEmail(),
                    request.getTicketId(),
                    request.getTypes(),
                    request.getStatuses(),
                    request.getEventSessionId(),
                    pageable);
            if (pageData == null || pageData.isEmpty()) {
                break;
            }
            hasData = true;
            List<AttendeeExportResponse> attendeeReports = pageData.stream().map(
                    attendee -> {
                        AttendeeExportResponse attendeeExport = attendeeExportMapper.toAttendeeExportResponse(attendee);

                        if (attendee.getCheckInAt() != null) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
                            attendeeExport.setCheckInAt(attendee.getCheckInAt().format(formatter));
                        }

                        attendeeExport.setType(attendee.getType() == AttendeeType.INVITE ? "Khách mời" : "Người tham gia");
                        attendeeExport.setStatus(attendee.getStatus() == AttendeeStatus.CHECKED_IN ? "Đã check-in"
                                : "Chưa check-in");
                        return attendeeExport;
                    }
            ).collect(Collectors.toList());

            excelWriter.write(attendeeReports, writeSheet);

            if (currentPage >= pageData.getTotalPages()) {
                hasNextPage = false;
            } else {
                currentPage++;
            }
        }
        if (!hasData) {
            excelWriter.write(new ArrayList<AttendeeExportResponse>(), writeSheet);
        }
        excelWriter.finish();

    }

    @Override
    public int countBoughtTicket(Long ticketId, Long userId) {
        return attendeeRepository.countBoughtTicket(ticketId, userId);
    }
}

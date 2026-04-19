package com.auhpp.event_management.service;

import com.alibaba.excel.ExcelWriter;
import com.auhpp.event_management.dto.request.*;
import com.auhpp.event_management.dto.response.*;

public interface AttendeeService {
    AttendeeResponse createAttendee(AttendeeCreateRequest attendeeCreateRequest);

    AttendeeResponse confirmValidAttendee(Long attendeeId);

    PageResponse<AttendeeResponse> getAttendeesByCurrentUser(AttendeeSearchRequest attendeeSearchRequest,
                                                             int page, int size);

    AttendeeResponse getAttendeeById(Long id);

    AttendeeResponse assignAttendeeEmail(Long id, String email);

    String getMeetingLink(Long attendeeId);

    PageResponse<AttendeeResponse> getAttendees(CheckinSearchRequest request, int page, int size);

    String generateTicketCode();

    AttendeeBasicResponse checkIn(CheckInRequest request);

    AttendeeBasicResponse cancelAttendee(Long id);

    PageResponse<UserAttendeeSummaryResponse>
    getUserAttendeeSummaries(Long eventSessionId,
                             AttendeeSearchRequest searchRequest, int page, int size);

    String getTicketCode(Long id);

    boolean getAttendeeByEventSessionIdAndCurrentUser(Long eventSessionId, Long userId);

    AttendanceImportCheckInResponse processAttendanceFromEmails(Long eventSessionId,
                                                                AttendanceImportRequest request);

    PageResponse<AttendeeCheckinResponse> getAttendeeCheckins(Long ticketId, String email, int page, int size);

    PageResponse<CheckInLogResponse> getCheckInLogs(CheckInLogSearchRequest request, int page, int size);

    void exportReportAttendees(ExcelWriter excelWriter, AttendeeSearchRequest request, String eventName);

    int countBoughtTicket(Long ticketId, Long userId);
}

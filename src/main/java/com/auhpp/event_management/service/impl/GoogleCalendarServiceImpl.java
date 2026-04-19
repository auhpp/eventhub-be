package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.EventType;
import com.auhpp.event_management.constant.MeetingPlatform;
import com.auhpp.event_management.constant.NotificationType;
import com.auhpp.event_management.constant.SyncStatus;
import com.auhpp.event_management.dto.request.AddCalendarRequest;
import com.auhpp.event_management.dto.request.NotificationRequest;
import com.auhpp.event_management.entity.AppUser;
import com.auhpp.event_management.entity.CalendarSyncMapping;
import com.auhpp.event_management.entity.Event;
import com.auhpp.event_management.entity.EventSession;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.repository.AppUserRepository;
import com.auhpp.event_management.repository.CalendarSyncMappingRepository;
import com.auhpp.event_management.repository.EventSessionRepository;
import com.auhpp.event_management.service.GoogleCalendarService;
import com.auhpp.event_management.service.NotificationService;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.EventDateTime;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class GoogleCalendarServiceImpl implements GoogleCalendarService {

    @NonFinal
    @Value("${app.google.client-id}")
    String clientId;

    @NonFinal
    @Value("${app.google.client-secret}")
    String clientSecret;

    EventSessionRepository eventSessionRepository;
    NotificationService notificationService;
    AppUserRepository appUserRepository;
    CalendarSyncMappingRepository calendarSyncMappingRepository;

    final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    final String TIMEZONE = "Asia/Ho_Chi_Minh";

    @Override
    @Transactional
    public void connectAndAddEvent(AddCalendarRequest request) throws GeneralSecurityException, IOException {
        EventSession eventSession = eventSessionRepository.findById(request.getEventSessionId()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );

        NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        // get token
        GoogleTokenResponse response = new GoogleAuthorizationCodeTokenRequest(
                transport, JSON_FACTORY,
                "https://oauth2.googleapis.com/token",
                clientId, clientSecret,
                request.getAuthCode(), "postmessage"
        ).execute();

        // create user config
        AppUser appUser = appUserRepository.findById(request.getUserId()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (response.getRefreshToken() != null) {
            appUser.setGoogleRefreshToken(response.getRefreshToken());
        }
        appUser.setGoogleSyncStatus(SyncStatus.CONNECTED);
        appUserRepository.save(appUser);

        createEventOnGoogle(appUser, eventSession, request, transport);
    }

    private void createEventOnGoogle(AppUser appUser, EventSession eventSession,
                                     AddCalendarRequest request, NetHttpTransport transport) {
        try {

            Event myEvent = eventSession.getEvent();
            GoogleCredential credential = new GoogleCredential.Builder()
                    .setTransport(transport)
                    .setJsonFactory(JSON_FACTORY)
                    .setClientSecrets(clientId, clientSecret)
                    .build()
                    .setRefreshToken(appUser.getGoogleRefreshToken());

            Calendar service = new Calendar.Builder(transport, JSON_FACTORY, credential)
                    .setApplicationName("EventHub App")
                    .build();

            com.google.api.services.calendar.model.Event googleEvent = new com.google.api.services.calendar.model.Event()
                    .setSummary(myEvent.getName() != null ? myEvent.getName() : "Sự kiện EventHub")
                    .setLocation(buildLocation(myEvent, eventSession))
                    .setDescription(buildDescription(myEvent, eventSession, request));

            // convert timezone
            googleEvent.setStart(convertLocalDateTimeToEventDateTime(eventSession.getStartDateTime()));
            googleEvent.setEnd(convertLocalDateTimeToEventDateTime(eventSession.getEndDateTime()));

            com.google.api.services.calendar.model.Event createdEvent =
                    service.events().insert("primary", googleEvent).execute();
            CalendarSyncMapping calendarSyncMapping = CalendarSyncMapping.builder()
                    .appUser(appUser)
                    .eventSession(eventSession)
                    .googleEventId(createdEvent.getId())
                    .build();
            calendarSyncMappingRepository.save(calendarSyncMapping);
        } catch (TokenResponseException e) {
            if (e.getDetails() != null && "invalid_grant".equals(e.getDetails().getError())) {
                handleDisconnect(appUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Async
    public void syncUpdatedEventToAllUsersInBackground(Long eventSessionId) {
        try {
            Optional<EventSession> eventSession = eventSessionRepository.findById(eventSessionId);
            if (eventSession.isEmpty()) return;
            Event myEvent = eventSession.get().getEvent();

            List<CalendarSyncMapping> calendarSyncMappings = calendarSyncMappingRepository.
                    findByEventSessionIdAndGoogleEventIdIsNotNull(eventSessionId);
            if (calendarSyncMappings.isEmpty()) return;

            NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();

            for (CalendarSyncMapping syncMapping : calendarSyncMappings) {
                AppUser user = syncMapping.getAppUser();

                if (user.getGoogleRefreshToken() == null || user.getGoogleSyncStatus() == SyncStatus.DISCONNECTED) {
                    continue;
                }
                try {
                    GoogleCredential credential = new GoogleCredential.Builder()
                            .setTransport(transport)
                            .setJsonFactory(JSON_FACTORY)
                            .setClientSecrets(clientId, clientSecret)
                            .build()
                            .setRefreshToken(user.getGoogleRefreshToken());

                    Calendar service = new Calendar.Builder(transport, JSON_FACTORY, credential)
                            .setApplicationName("EventHub App")
                            .build();

                    // download current event of user
                    com.google.api.services.calendar.model.Event googleEvent = service.events().get("primary",
                            syncMapping.getGoogleEventId()).execute();

                    // update info
                    googleEvent.setSummary(myEvent.getName());
                    googleEvent.setLocation(buildLocation(myEvent, eventSession.get()));
                    googleEvent.setStart(convertLocalDateTimeToEventDateTime(eventSession.get().getStartDateTime()));
                    googleEvent.setEnd(convertLocalDateTimeToEventDateTime(eventSession.get().getEndDateTime()));

                    // upload to google calendar
                    service.events().update("primary", syncMapping.getGoogleEventId(), googleEvent).execute();
                } catch (TokenResponseException e) {
                    if (e.getDetails() != null && "invalid_grant".equals(e.getDetails().getError())) {
                        handleDisconnect(syncMapping.getAppUser());
                    }
                } catch (Exception e) {
                    System.err.println("Lỗi đồng bộ lịch cho User " + syncMapping.getAppUser().getId()
                            + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khởi tạo Background Task: " + e.getMessage());
        }
    }

    private String buildLocation(Event myEvent, EventSession eventSession) {
        if (myEvent.getType() == EventType.ONLINE) {
            return (eventSession.getMeetingPlatform() == MeetingPlatform.GOOGLE_MEET) ? "Google Meet" : "Zoom";
        } else {
            String address = (myEvent.getAddress() != null && !myEvent.getAddress().isEmpty()) ? myEvent.getAddress() + ", " : "";
            String location = (myEvent.getLocation() != null && !myEvent.getLocation().isEmpty()) ? myEvent.getLocation() : "Đang cập nhật địa điểm";
            return address + location;
        }
    }

    private String buildDescription(Event myEvent, EventSession eventSession, AddCalendarRequest request) {
        StringBuilder descriptionText = new StringBuilder();
        Integer count = request.getTicketCount() != null ? request.getTicketCount() : 1;

        descriptionText.append("Bạn có ").append(count)
                .append(" vé tham gia sự kiện này.\n\n");

        if (request.getBookingId() != null) {
            descriptionText.append("Xem chi tiết vé của bạn tại: http://localhost:3000/my-tickets/")
                    .append(request.getBookingId()).append("\n");
        }

        if (myEvent.getType() == EventType.ONLINE) {
            descriptionText.append("\n--- THÔNG TIN THAM GIA ONLINE ---\n");
            if (eventSession.getMeetingUrl() != null && !eventSession.getMeetingUrl().isEmpty()) {
                descriptionText.append("Link tham gia: ").append(eventSession.getMeetingUrl()).append("\n");
            }
            if (eventSession.getMeetingPassword() != null && !eventSession.getMeetingPassword().isEmpty()) {
                descriptionText.append("Mật khẩu phòng: ").append(eventSession.getMeetingPassword()).append("\n");
            }
        }

        return descriptionText.toString();
    }

    private EventDateTime convertLocalDateTimeToEventDateTime(java.time.LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.of(TIMEZONE);
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return new EventDateTime()
                .setDateTime(new DateTime(Date.from(zdt.toInstant())))
                .setTimeZone(TIMEZONE);
    }

    private void handleDisconnect(AppUser appUser) {
        appUser.setGoogleSyncStatus(SyncStatus.DISCONNECTED);
        appUser.setGoogleRefreshToken(null);
        appUserRepository.save(appUser);

        notificationService.createNotification(
                NotificationRequest.builder()
                        .message("Phiên đăng nhập Google của bạn đã bị thu hồi hoặc hết hạn." +
                                " Vui lòng kết nối lại lịch để không bỏ lỡ thông báo khi sự kiện thay đổi.")
                        .recipientIds(List.of(appUser.getId()))
                        .type(NotificationType.EXPIRED_CALENDAR)
                        .build()
        );

    }
}

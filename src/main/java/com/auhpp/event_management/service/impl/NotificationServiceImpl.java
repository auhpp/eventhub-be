package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.dto.request.NotificationRequest;
import com.auhpp.event_management.dto.response.NotificationResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.entity.AppUser;
import com.auhpp.event_management.entity.Notification;
import com.auhpp.event_management.entity.NotificationRecipient;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.NotificationMapper;
import com.auhpp.event_management.repository.AppUserRepository;
import com.auhpp.event_management.repository.NotificationRecipientRepository;
import com.auhpp.event_management.repository.NotificationRepository;
import com.auhpp.event_management.service.NotificationSenderService;
import com.auhpp.event_management.service.NotificationService;
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

import java.util.List;
import java.util.Objects;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    NotificationRepository notificationRepository;
    NotificationMapper notificationMapper;
    AppUserRepository appUserRepository;
    NotificationSenderService notificationSenderService;
    NotificationRecipientRepository notificationRecipientRepository;

    @Override
    @Transactional
    public void createNotification(NotificationRequest notificationRequest) {
        Notification notification = notificationMapper.toNotification(notificationRequest);
        List<NotificationRecipient> recipients = notificationRequest.getRecipientIds().stream().map(
                recipientId -> {
                    AppUser user = appUserRepository.findById(recipientId).orElseThrow(
                            () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
                    );
                    return NotificationRecipient.builder()
                            .notification(notification)
                            .appUser(user)
                            .build();
                }
        ).toList();
        notification.setNotificationRecipients(recipients);
        notificationRepository.save(notification);

        NotificationResponse response = notificationMapper.toNotificationResponse(notification);
        notificationSenderService.pushNotificationToClients(response, notificationRequest.getRecipientIds());

    }

    @Override
    public PageResponse<NotificationResponse> getNotifications(int page, int size) {
        String email = SecurityUtils.getCurrentUserLogin();
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC,
                "createdAt"));
        Page<NotificationRecipient> pageData = notificationRecipientRepository.findAllByEmail(email, pageable);
        List<NotificationResponse> responses = pageData.getContent().stream().map(
                notificationRecipient -> {
                    NotificationResponse notificationRes = notificationMapper.toNotificationResponse(
                            notificationRecipient.getNotification());
                    notificationRes.setSeen(notificationRecipient.isSeen());
                    return notificationRes;
                }
        ).toList();
        return PageResponse.<NotificationResponse>builder()
                .currentPage(page)
                .totalElements(pageData.getTotalElements())
                .totalPage(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .data(responses)
                .build();
    }

    @Override
    public void deleteNotification(Long id) {
        NotificationRecipient notificationRecipient = notificationRecipientRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (!Objects.equals(notificationRecipient.getAppUser().getEmail(), SecurityUtils.getCurrentUserLogin())) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        notificationRecipientRepository.deleteById(id);
    }

    @Override
    public NotificationResponse seenNotification(Long id) {
        NotificationRecipient notificationRecipient = notificationRecipientRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (!Objects.equals(notificationRecipient.getAppUser().getEmail(), SecurityUtils.getCurrentUserLogin())) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        notificationRecipient.setSeen(true);
        notificationRecipientRepository.save(notificationRecipient);
        NotificationResponse response = notificationMapper.toNotificationResponse(notificationRecipient.getNotification());
        response.setSeen(notificationRecipient.isSeen());
        return response;
    }

    @Override
    public Integer countUnseen() {
        return notificationRecipientRepository.countUnseen(SecurityUtils.getCurrentUserLogin());
    }
}

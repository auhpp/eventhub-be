package com.auhpp.event_management.controller;

import com.auhpp.event_management.dto.response.NotificationResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {
    NotificationService notificationService;

    @GetMapping
    public ResponseEntity<PageResponse<NotificationResponse>> getNotifications(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        PageResponse<NotificationResponse> res = notificationService.getNotifications(
                page, size
        );
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PostMapping("/seen/{id}")
    public ResponseEntity<NotificationResponse> seenNotification(
            @PathVariable(name = "id") Long id
    ) {
        NotificationResponse notification = notificationService.seenNotification(id);
        return ResponseEntity.status(HttpStatus.OK).body(notification);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable("id") Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> countSeen() {
        Integer count = notificationService.countUnseen();
        return ResponseEntity.status(HttpStatus.OK).body(count);
    }
}

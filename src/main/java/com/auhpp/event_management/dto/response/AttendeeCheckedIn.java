package com.auhpp.event_management.dto.response;

public interface AttendeeCheckedIn {
    Long getUserId();

    String getEmail();

    String getFullName();

    String getAvatar();

    int getCheckedInCount();
}

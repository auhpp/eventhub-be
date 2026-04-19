package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.AddCalendarRequest;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface GoogleCalendarService {
    void connectAndAddEvent(AddCalendarRequest request) throws GeneralSecurityException, IOException;

    void syncUpdatedEventToAllUsersInBackground(Long eventSessionId);
}

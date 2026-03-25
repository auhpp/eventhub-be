package com.auhpp.event_management.service;

public interface OnlineOfflineService {
    void addOnlineUser(String userEmail, String sessionId);

    void removeOnlineUser(String userEmail, String sessionId);

    void extendExpire(String userEmail);
}

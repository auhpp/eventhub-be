package com.auhpp.event_management.service;

import java.util.List;

public interface FavoriteService {
    void create(Long eventId);

    void delete(Long eventId);

    List<Long> getEventIdFavorite();
}

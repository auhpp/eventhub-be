package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.entity.AppUser;
import com.auhpp.event_management.entity.Event;
import com.auhpp.event_management.entity.Favorite;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.repository.AppUserRepository;
import com.auhpp.event_management.repository.EventRepository;
import com.auhpp.event_management.repository.FavoriteRepository;
import com.auhpp.event_management.service.FavoriteService;
import com.auhpp.event_management.util.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    FavoriteRepository favoriteRepository;
    AppUserRepository appUserRepository;
    EventRepository eventRepository;

    @Override
    @Transactional
    public void create(Long eventId) {
        // find current user
        AppUser appUser = appUserRepository.findByEmail(SecurityUtils.getCurrentUserLogin()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        // find exist
        boolean exists = favoriteRepository.existsByEventIdAndAppUserId(eventId, appUser.getId());
        // create favorite
        if (!exists) {
            Event event = eventRepository.findById(eventId).orElseThrow(
                    () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
            );
            favoriteRepository.save(Favorite.builder()
                    .event(event)
                    .appUser(appUser)
                    .build());
        }
    }

    @Override
    @Transactional
    public void delete(Long eventId) {
        // find current user
        AppUser appUser = appUserRepository.findByEmail(SecurityUtils.getCurrentUserLogin()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        favoriteRepository.deleteByEventIdAndAppUserId(eventId, appUser.getId());
    }

    @Override
    public List<Long> getEventIdFavorite() {
        String currentUserEmail = SecurityUtils.getCurrentUserLogin();
        return favoriteRepository.getFavoriteEventIds(currentUserEmail);
    }
}

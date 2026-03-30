package com.auhpp.event_management.repository;

import com.auhpp.event_management.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsByEventIdAndAppUserId(Long eventId, Long appUserId);

    void deleteByEventIdAndAppUserId(Long eventId, Long appUserId);

    @Query("SELECT f.event.id FROM Favorite f " +
            "WHERE f.appUser.email = :userEmail")
    List<Long> getFavoriteEventIds(@Param("userEmail") String userEmail);
}

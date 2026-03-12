package com.auhpp.event_management.repository;

import com.auhpp.event_management.entity.EventSeries;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventSeriesRepository extends JpaRepository<EventSeries, Long> {

    @Query("SELECT DISTINCT es FROM EventSeries es " +
            "LEFT JOIN es.eventSeriesFollowers ef " +
            "WHERE (:userId IS NULL OR es.appUser.id = :userId) " +
            "AND (:userFollowerId IS NULL OR ef.appUser.id = :userFollowerId)")
    Page<EventSeries> filterEventSeries(@Param("userId") Long userId,
                                        @Param("userFollowerId") Long userFollowerId,
                                        Pageable pageable);

    @Query("SELECT es FROM EventSeries es " +
            "WHERE  es.appUser.email = :userEmail")
    List<EventSeries> findAllByUserEmail(@Param("userEmail") String email);
}

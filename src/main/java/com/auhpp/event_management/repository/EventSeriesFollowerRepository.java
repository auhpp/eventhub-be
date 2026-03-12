package com.auhpp.event_management.repository;

import com.auhpp.event_management.entity.EventSeriesFollower;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventSeriesFollowerRepository extends JpaRepository<EventSeriesFollower, Long> {
    @Query("SELECT esf from EventSeriesFollower esf " +
            "WHERE (:userId IS NULL OR esf.appUser.id = :userId) " +
            "AND (:eventSeriesId IS NULL OR esf.eventSeries.id = :eventSeriesId) ")
    Page<EventSeriesFollower> filterEventSeriesFollower(@Param("userId") Long userId,
                                                        @Param("eventSeriesId") Long eventSeriesId,
                                                        Pageable pageable);

    Optional<EventSeriesFollower> findByAppUserIdAndEventSeriesId(
            @Param("userId") Long userId,
            @Param("eventSeriesId") Long eventSeriesId
    );

    @Query("SELECT COUNT(esf) from EventSeriesFollower esf " +
            "WHERE (:userId IS NULL OR esf.appUser.id = :userId) " +
            "AND (:eventSeriesId IS NULL OR esf.eventSeries.id = :eventSeriesId) ")
    Integer countEventSeriesFollower(@Param("userId") Long userId,
                                     @Param("eventSeriesId") Long eventSeriesId);
}

package com.auhpp.event_management.repository;

import com.auhpp.event_management.constant.EventSeriesStatus;
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
            "WHERE (:userId IS NULL OR es.appUser.id = :userId) " +
            "AND (:userFollowerId IS NULL OR exists (" +
            "SELECT 1 FROM EventSeriesFollower ef " +
            "WHERE ef.eventSeries.id = es.id AND ef.appUser.id = :userFollowerId))" +
            "AND (:statuses IS NULL OR es.status IN :statuses)" +
            "AND (:name IS NULL OR (" +
            "LOWER(es.appUser.fullName)" +
            "   LIKE LOWER(CONCAT('%', CAST(:name AS string), '%')))" +
            "OR LOWER(es.name)" +
            " LIKE LOWER(CONCAT('%', CAST(:name AS string), '%')))")
    Page<EventSeries> filterEventSeries(
            @Param("name") String name,
            @Param("userId") Long userId,
            @Param("userFollowerId") Long userFollowerId,
            @Param("statuses") List<EventSeriesStatus> statuses,
            Pageable pageable);

    @Query("SELECT es FROM EventSeries es " +
            "WHERE  es.appUser.email = :userEmail")
    List<EventSeries> findAllByUserEmail(@Param("userEmail") String email);
}

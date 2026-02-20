package com.auhpp.event_management.repository;

import com.auhpp.event_management.constant.EventStatus;
import com.auhpp.event_management.constant.EventType;
import com.auhpp.event_management.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>,
        JpaSpecificationExecutor<Event> {

    @Query("SELECT e FROM Event e " +
            "LEFT JOIN e.eventStaffs es " +
            "WHERE (:userId IS NULL OR es.appUser.id = :userId) " +
            "AND (:status IS NULL OR e.status = :status) " +
            "AND (:eventType IS NULL OR e.type = :eventType) ")
    Page<Event> findAllByUserIdAndStatus(@Param("userId") Long userId,
                                         @Param("status") EventStatus status,
                                         @Param("eventType") EventType type,
                                         Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "LEFT JOIN e.eventStaffs es " +
            "WHERE (:userId IS NULL OR es.appUser.id = :userId) " +
            "AND (:status IS NULL OR e.status = :status) " +
            "AND (:eventType IS NULL OR e.type = :eventType) " +
            "AND (CAST(:currentDate AS timestamp) IS NULL OR e.id IN ( " +
            "  SELECT ess.event.id FROM EventSession ess " +
            "  WHERE ess.event.id = e.id" +
            "  AND ess.startDateTime > :currentDate)) ")
    Page<Event> findAllByUserIdAndStatusAndComingStatus(@Param("userId") Long userId,
                                                        @Param("status") EventStatus status,
                                                        @Param("currentDate") LocalDateTime currentDate,
                                                        @Param("eventType") EventType type,
                                                        Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "LEFT JOIN e.eventStaffs es " +
            "WHERE (:userId IS NULL OR es.appUser.id = :userId) " +
            "AND (:status IS NULL OR e.status = :status) " +
            "AND (:eventType IS NULL OR e.type = :eventType) " +
            "AND (CAST(:currentDate AS timestamp) IS NULL OR SIZE(e.eventSessions) = ( " +
            "  SELECT COUNT(ess) FROM EventSession ess " +
            "  WHERE ess.event.id = e.id " +
            "  AND ess.endDateTime < :currentDate)) ")
    Page<Event> findAllByUserIdAndStatusAndPastStatus(@Param("userId") Long userId,
                                                      @Param("status") EventStatus status,
                                                      @Param("currentDate") LocalDateTime currentDate,
                                                      @Param("eventType") EventType type,
                                                      Pageable pageable);
}

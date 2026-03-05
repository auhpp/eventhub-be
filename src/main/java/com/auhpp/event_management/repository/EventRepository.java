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
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>,
        JpaSpecificationExecutor<Event> {

    @Query("SELECT DISTINCT e FROM Event e " +
            "LEFT JOIN e.eventStaffs es " +
            "LEFT JOIN e.eventSessions evs " +
            "LEFT JOIN evs.tickets t " +
            "WHERE (:userId IS NULL OR es.appUser.id = :userId) " +
            "AND (:status IS NULL OR e.status = :status) " +
            "AND (:eventType IS NULL OR e.type = :eventType) " +
            "AND (:categoryIds IS NULL OR e.category.id IN :categoryIds) " +
            "AND (CAST(:startDate AS timestamp ) IS NULL OR evs.startDateTime >= :startDate) " +
            "AND (CAST(:endDate AS timestamp ) IS NULL OR evs.endDateTime <= :endDate) " +
            "AND (:priceFrom IS NULL OR t.price >= :priceFrom) " +
            "AND (:priceTo IS NULL OR t.price <= :priceTo) " +
            "AND (CAST(:name AS string ) IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', CAST(:name AS string), '%'))) ")
    Page<Event> filterEvents(@Param("userId") Long userId,
                             @Param("status") EventStatus status,
                             @Param("eventType") EventType type,
                             @Param("startDate") LocalDateTime startDate,
                             @Param("endDate") LocalDateTime endDate,
                             @Param("categoryIds") List<Long> categoryIds,
                             @Param("priceFrom") Double priceFrom,
                             @Param("priceTo") Double priceTo,
                             @Param("name") String name,
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

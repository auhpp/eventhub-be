package com.auhpp.event_management.repository;

import com.auhpp.event_management.constant.EventStatus;
import com.auhpp.event_management.constant.EventType;
import com.auhpp.event_management.entity.Event;
import com.auhpp.event_management.repository.custom.EventCustomRepository;
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
        JpaSpecificationExecutor<Event>, EventCustomRepository {

    @Query("SELECT e FROM Event e " +
            "WHERE (:status IS NULL OR e.status = :status) " +
            "AND (:eventType IS NULL OR e.type = :eventType) " +
            "AND (:categoryIds IS NULL OR e.category.id IN :categoryIds) " +
            "AND (:eventSeriesId IS NULL OR e.eventSeries.id = :eventSeriesId) " +
            "AND (:email IS NULL OR e.appUser.email = :email) " +
            "AND (CAST(:name AS string) IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', CAST(:name AS string), '%'))) " +

            "AND (:userId IS NULL OR EXISTS (SELECT 1 FROM e.eventStaffs es WHERE es.appUser.id = :userId)) " +

            "AND (CAST(:startDate AS timestamp) IS NULL AND CAST(:endDate AS timestamp) IS NULL OR EXISTS (" +
            "   SELECT 1 FROM e.eventSessions evs " +
            "   WHERE (CAST(:startDate AS timestamp) IS NULL OR evs.startDateTime >= :startDate) " +
            "   AND (CAST(:endDate AS timestamp) IS NULL OR evs.endDateTime <= :endDate)" +
            ")) " +

            "AND (:priceFrom IS NULL AND :priceTo IS NULL OR EXISTS (" +
            "   SELECT 1 FROM e.eventSessions evs JOIN evs.tickets t " +
            "   WHERE (:priceFrom IS NULL OR t.price >= :priceFrom) " +
            "   AND (:priceTo IS NULL OR t.price <= :priceTo)" +
            ")) " +

            "AND (:hasResale IS NULL OR " +
            "   EXISTS (SELECT 1 FROM e.eventSessions evs JOIN evs.tickets t JOIN t.attendees a" +
            " WHERE a.resalePost IS NOT NULL AND a.resalePost.status = 'APPROVED') " +
            "   AND EXISTS (SELECT 1 FROM e.eventSessions evsValid WHERE evsValid.endDateTime > CURRENT_TIMESTAMP)" +
            ") " +

            "AND (:hasFavorite IS NULL OR EXISTS (" +
            "   SELECT 1 FROM Favorite f WHERE f.event = e AND f.appUser.id = :currentUserId" +
            "))" +
            "AND (:slug IS NULL OR exists (" +
            "SELECT 1 FROM e.eventTags et " +
            "WHERE et.tag.slug = :slug))")
    Page<Event> filterEvents(@Param("userId") Long userId,
                             @Param("status") EventStatus status,
                             @Param("eventType") EventType type,
                             @Param("startDate") LocalDateTime startDate,
                             @Param("endDate") LocalDateTime endDate,
                             @Param("categoryIds") List<Long> categoryIds,
                             @Param("priceFrom") Double priceFrom,
                             @Param("priceTo") Double priceTo,
                             @Param("name") String name,
                             @Param("eventSeriesId") Long eventSeriesId,
                             @Param("hasResale") Boolean hasResale,
                             @Param("currentUserId") Long currentUserId,
                             @Param("hasFavorite") Boolean hasFavorite,
                             @Param("email") String email,
                             @Param("slug") String slug,
                             Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "LEFT JOIN e.eventStaffs es " +
            "LEFT JOIN e.eventSessions evs " +
            "WHERE (:userId IS NULL OR es.appUser.id = :userId) " +
            "AND (:statuses IS NULL OR e.status IN :statuses) " +
            "AND (:eventType IS NULL OR e.type = :eventType) " +
            "AND (:eventSeriesId IS NULL OR e.eventSeries.id = :eventSeriesId) " +
            "AND (CAST(:name AS string ) IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', CAST(:name AS string), '%'))) " +
            "AND (CAST(:startDate AS timestamp ) IS NULL OR evs.startDateTime >= :startDate) " +
            "AND (CAST(:endDate AS timestamp ) IS NULL OR evs.endDateTime <= :endDate) " +
            "AND (CAST(:currentDate AS timestamp) IS NULL OR e.id IN ( " +
            "  SELECT ess.event.id FROM EventSession ess " +
            "  WHERE ess.event.id = e.id" +
            "  AND ess.startDateTime > :currentDate)) ")
    Page<Event> findAllByUserIdAndStatusAndComingStatus(@Param("userId") Long userId,
                                                        @Param("statuses") List<EventStatus> statuses,
                                                        @Param("currentDate") LocalDateTime currentDate,
                                                        @Param("eventType") EventType type,
                                                        @Param("eventSeriesId") Long eventSeriesId,
                                                        @Param("startDate") LocalDateTime startDate,
                                                        @Param("endDate") LocalDateTime endDate,
                                                        @Param("name") String name,
                                                        Pageable pageable);

    @Query("SELECT DISTINCT e FROM Event e " +
            "LEFT JOIN e.eventStaffs es " +
            "LEFT JOIN e.eventSessions evs " +
            "WHERE (:userId IS NULL OR es.appUser.id = :userId) " +
            "AND (:statuses IS NULL OR e.status IN :statuses) " +
            "AND (:eventType IS NULL OR e.type = :eventType) " +
            "AND (:eventSeriesId IS NULL OR e.eventSeries.id = :eventSeriesId) " +
            "AND (CAST(:name AS string ) IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', CAST(:name AS string), '%'))) " +
            "AND (CAST(:startDate AS timestamp ) IS NULL OR evs.startDateTime >= :startDate) " +
            "AND (CAST(:endDate AS timestamp ) IS NULL OR evs.endDateTime <= :endDate) " +
            "AND (CAST(:currentDate AS timestamp) IS NULL OR SIZE(e.eventSessions) = ( " +
            "  SELECT COUNT(ess) FROM EventSession ess " +
            "  WHERE ess.event.id = e.id " +
            "  AND ess.endDateTime < :currentDate)) ")
    Page<Event> findAllByUserIdAndStatusAndPastStatus(@Param("userId") Long userId,
                                                      @Param("statuses") List<EventStatus> statuses,
                                                      @Param("currentDate") LocalDateTime currentDate,
                                                      @Param("eventType") EventType type,
                                                      @Param("eventSeriesId") Long eventSeriesId,
                                                      @Param("startDate") LocalDateTime startDate,
                                                      @Param("endDate") LocalDateTime endDate,
                                                      @Param("name") String name,

                                                      Pageable pageable);

    @Query("SELECT COUNT(e) FROM Event e " +
            "WHERE (:categoryId IS NULL OR e.category.id = :categoryId) " +
            "AND (:organizerId IS NULL OR e.appUser.id = :organizerId) " +
            "AND (:statuses IS NULL OR e.status IN :statuses) " +
            "AND (CAST(:startDate AS timestamp) IS NULL OR e.createdAt >= :startDate) " +
            "AND (CAST(:endDate AS timestamp) IS NULL OR e.createdAt <= :endDate)")
    Integer countEvent(
            @Param("organizerId") Long organizerId,
            @Param("categoryId") Long categoryId,
            @Param("statuses") List<EventStatus> statuses,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);


    @Query("SELECT COUNT(DISTINCT e.id) FROM Event e " +
            "WHERE (:userId IS NULL OR e.appUser.id = :userId) " +
            "AND (:statuses IS NULL OR e.status IN :statuses) " +
            "AND (CAST(:startDate AS timestamp ) IS NULL OR e.createdAt >= :startDate) " +
            "AND (CAST(:endDate AS timestamp ) IS NULL OR e.createdAt <= :endDate) " +
            "AND e.id IN ( " +
            "  SELECT ess.event.id FROM EventSession ess " +
            "  WHERE ess.event.id = e.id" +
            "  AND ess.startDateTime > current_timestamp ) ")
    int countUpcoming(
            @Param("userId") Long userId,
            @Param("statuses") List<EventStatus> statuses,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(DISTINCT e.id) FROM Event e " +
            "WHERE (:userId IS NULL OR e.appUser.id = :userId) " +
            "AND (:statuses IS NULL OR e.status IN :statuses) " +
            "AND (CAST(:startDate AS timestamp ) IS NULL OR e.createdAt >= :startDate) " +
            "AND (CAST(:endDate AS timestamp ) IS NULL OR e.createdAt <= :endDate) " +
            "AND  e.id IN ( " +
            "  SELECT ess.event.id FROM EventSession ess " +
            "  WHERE ess.event.id = e.id" +
            "  AND ess.endDateTime < current_timestamp ) ")
    int countPast(
            @Param("userId") Long userId,
            @Param("statuses") List<EventStatus> statuses,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(e) FROM Event e " +
            "WHERE (:organizerId IS NULL OR e.appUser.id = :organizerId) " +
            "AND (:statuses IS NULL OR e.status IN :statuses) " +
            "AND (CAST(:startDate AS timestamp) IS NULL OR e.createdAt >= :startDate) " +
            "AND (CAST(:endDate AS timestamp) IS NULL OR e.createdAt <= :endDate) " +
            "AND EXISTS (" +
            "   SELECT 1 FROM EventSession es " +
            "   WHERE es.event.id = e.id " +
            "   AND EXISTS (" +
            "   SELECT 1 FROM Ticket t " +
            "   WHERE t.eventSession.id = es.id " +
            "   AND t.openAt > current_timestamp " +
            "   AND t.endAt > current_timestamp ))")
    int countActiveEvent(
            @Param("organizerId") Long organizerId,
            @Param("statuses") List<EventStatus> statuses,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT e.hasResalable FROM Event e " +
            "WHERE e.id = :eventId")
    Boolean getHasResalable(@Param("eventId") Long eventId);
}

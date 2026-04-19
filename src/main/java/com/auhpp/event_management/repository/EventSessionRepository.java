package com.auhpp.event_management.repository;

import com.auhpp.event_management.dto.response.ChartDataProjection;
import com.auhpp.event_management.entity.EventSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventSessionRepository extends JpaRepository<EventSession, Long> {


    @Query("SELECT COALESCE(SUM(t.price * t.quantity), 0) FROM Ticket t " +
            "WHERE (:eventSessionId IS NULL OR t.eventSession.id = :eventSessionId) " +
            "AND (:eventSeriesId IS NULL OR t.eventSession.event.eventSeries.id = :eventSeriesId)" +
            "")
    Double getMaxPotentialRevenue(
            @Param("eventSeriesId") Long eventSeriesId,
            @Param("eventSessionId") Long eventSessionId);

    @Query("SELECT COALESCE(SUM(t.soldQuantity), 0) FROM Ticket t " +
            "WHERE (:eventSessionId IS NULL OR t.eventSession.id = :eventSessionId) " +
            "AND (:eventSeriesId IS NULL OR t.eventSession.event.eventSeries.id = :eventSeriesId)" +
            "AND (CAST(:startDate AS timestamp) IS NULL OR exists (" +
            "SELECT 1 FROM Attendee a " +
            "WHERE a.ticket.id = t.id AND a.createdAt >= :startDate))" +
            "AND (CAST(:endDate AS timestamp) IS NULL OR exists (" +
            "SELECT 1 FROM Attendee a " +
            "WHERE a.ticket.id = t.id AND a.createdAt <= :endDate))")
    Integer getTotalTicketsSold(
            @Param("eventSeriesId") Long eventSeriesId,
            @Param("eventSessionId") Long eventSessionId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COALESCE(SUM(t.quantity), 0) FROM Ticket t " +
            "WHERE (:eventSessionId IS NULL OR t.eventSession.id = :eventSessionId) " +
            "AND (:eventSeriesId IS NULL OR t.eventSession.event.eventSeries.id = :eventSeriesId)" +
            "AND (CAST(:startDate AS timestamp) IS NULL OR exists (" +
            "SELECT 1 FROM Attendee a " +
            "WHERE a.ticket.id = t.id AND a.createdAt >= :startDate))" +
            "AND (CAST(:endDate AS timestamp) IS NULL OR exists (" +
            "SELECT 1 FROM Attendee a " +
            "WHERE a.ticket.id = t.id AND a.createdAt <= :endDate))")
    Integer getTotalTicketCapacity(
            @Param("eventSeriesId") Long eventSeriesId,
            @Param("eventSessionId") Long eventSessionId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);


    @Query("SELECT COALESCE(SUM(b.discountAmount), 0) FROM Booking b " +
            "WHERE (:eventSeriesId IS NULL OR exists ( " +
            " SELECT 1 FROM Attendee a " +
            " WHERE a.booking.id = b.id AND a.ticket.eventSession.event.eventSeries.id = :eventSeriesId))" +
            "AND (:eventSessionId IS NULL OR exists ( " +
            " SELECT 1 FROM Attendee a " +
            " WHERE a.booking.id = b.id AND a.ticket.eventSession.id = :eventSessionId))" +
            "AND (CAST(:startDate AS timestamp) IS NULL OR b.createdAt >= :startDate) " +
            "AND (CAST(:endDate AS timestamp) IS NULL OR b.createdAt <= :endDate)")
    Double getDiscountAmount(
            @Param("eventSeriesId") Long eventSeriesId,
            @Param("eventSessionId") Long eventSessionId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);


    @Query(value = """
            SELECT 
                            TO_CHAR(b.created_at, 'DD/MM') as timeLabel,
                            COALESCE(SUM(b.final_amount), 0) AS revenue,
                            COALESCE(SUM(
                                        (SELECT COUNT(a.id) 
                                        FROM attendee a
                                        INNER JOIN ticket t ON a.ticket_id = t.id
                                        WHERE a.booking_id = b.id 
                                        AND t.event_session_id = :eventSessionId
                                        AND b.type != 'INVITE' 
                                        )
                            ), 0) AS ticketsSold
            FROM booking b
            WHERE b.status = 'PAID'
            AND b.type = 'BUY'
            AND b.created_at >= :startDate
            AND EXISTS(
                    SELECT 1
                    FROM attendee a
                    INNER JOIN ticket t ON a.ticket_id = t.id
                    WHERE a.booking_id = b.id AND t.event_session_id = :eventSessionId
            )
            GROUP BY TO_CHAR(b.created_at, 'DD/MM'), DATE(b.created_at)
            ORDER BY DATE(b.created_at) ASC
            """,
            nativeQuery = true)
    List<ChartDataProjection> getChartDataByDay(@Param("eventSessionId") Long eventSessionId,
                                                @Param("startDate") LocalDateTime startDate);

    @Query(value = """
            SELECT 
                            TO_CHAR(b.created_at, 'HH24:00') as timeLabel,
                            COALESCE(SUM(b.final_amount), 0) AS revenue,
                            COALESCE(SUM(
                                        (SELECT COUNT(a.id) 
                                        FROM attendee a
                                        INNER JOIN ticket t ON a.ticket_id = t.id
                                        WHERE a.booking_id = b.id 
                                        AND t.event_session_id = :eventSessionId
                                        AND b.type != 'INVITE' 
                                        )
                            ), 0) AS ticketsSold
            FROM booking b
            WHERE b.status = 'PAID'
            AND b.created_at >= :startDate
            AND b.type = 'BUY'
            AND EXISTS(
                    SELECT 1
                    FROM attendee a
                    INNER JOIN ticket t ON a.ticket_id = t.id
                    WHERE a.booking_id = b.id AND t.event_session_id = :eventSessionId
            )
            GROUP BY TO_CHAR(b.created_at, 'HH24:00'), EXTRACT(HOUR FROM b.created_at), DATE(b.created_at)
            ORDER BY DATE(b.created_at) ASC, EXTRACT(HOUR FROM b.created_at) ASC
            """,
            nativeQuery = true)
    List<ChartDataProjection> getChartDataByHour(@Param("eventSessionId") Long eventSessionId,
                                                 @Param("startDate") LocalDateTime startDate);

}

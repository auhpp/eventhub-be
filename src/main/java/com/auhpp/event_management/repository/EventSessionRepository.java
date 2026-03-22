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

    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM Booking b " +
            "WHERE b.id IN ( " +
            " SELECT b2.id FROM Booking b2 " +
            " JOIN b2.attendees a " +
            " WHERE a.ticket.eventSession.id = :eventSessionId )")
    Double getTotalRevenue(@Param("eventSessionId") Long eventSessionId);

    @Query("SELECT COALESCE(SUM(t.price * t.quantity), 0) FROM Ticket t " +
            "WHERE t.eventSession.id = :eventSessionId ")
    Double getMaxPotentialRevenue(@Param("eventSessionId") Long eventSessionId);

    @Query("SELECT COALESCE(SUM(t.soldQuantity), 0) FROM Ticket t " +
            "WHERE t.eventSession.id = :eventSessionId ")
    Integer getTotalTicketsSold(@Param("eventSessionId") Long eventSessionId);

    @Query("SELECT COALESCE(SUM(t.quantity), 0) FROM Ticket t " +
            "WHERE t.eventSession.id = :eventSessionId ")
    Integer getTotalTicketCapacity(@Param("eventSessionId") Long eventSessionId);

    @Query("SELECT COALESCE(SUM(b.finalAmount), 0) FROM Booking b " +
            "WHERE b.id IN ( " +
            " SELECT b2.id FROM Booking b2 " +
            " JOIN b2.attendees a " +
            " WHERE a.ticket.eventSession.id = :eventSessionId )")
    Double getVoucherRevenue(@Param("eventSessionId") Long eventSessionId);

    @Query("SELECT COALESCE(SUM(b.discountAmount), 0) FROM Booking b " +
            "WHERE b.id IN ( " +
            " SELECT b2.id FROM Booking b2 " +
            " JOIN b2.attendees a " +
            " WHERE a.ticket.eventSession.id = :eventSessionId )")
    Double getDiscountAmount(@Param("eventSessionId") Long eventSessionId);

    @Query("SELECT COALESCE(SUM(a.price * a.ticket.eventSession.event.commissionRate + " +
            "a.ticket.eventSession.event.commissionFixedPerTicket), 0) FROM Attendee a " +
            "WHERE a.ticket.eventSession.id = :eventSessionId ")
    Double getTotalFee(@Param("eventSessionId") Long eventSessionId);

    @Query(value = """
            SELECT 
                            TO_CHAR(b.created_at, 'DD/MM') as timeLabel,
                            COALESCE(SUM(b.final_amount), 0) AS revenue,
                            COALESCE(SUM(
                                        (SELECT COUNT(a.id) 
                                        FROM attendee a
                                        INNER JOIN ticket t ON a.ticket_id = t.id
                                        WHERE a.booking_id = b.id AND t.event_session_id = :eventSessionId
                                        )
                            ), 0) AS ticketsSold
            FROM booking b
            WHERE b.status = 'PAID'
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
                                        WHERE a.booking_id = b.id AND t.event_session_id = :eventSessionId
                                        )
                            ), 0) AS ticketsSold
            FROM booking b
            WHERE b.status = 'PAID'
            AND b.created_at >= :startDate
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

package com.auhpp.event_management.repository;

import com.auhpp.event_management.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT t FROM Ticket t " +
            "JOIN t.eventSession es " +
            "WHERE es.event.id = :eventId")
    List<Ticket> findByEventId(@Param("eventId") Long eventId);

    List<Ticket> findByEventSessionId(Long eventSessionId);

    @Query("SELECT COALESCE(SUM(t.soldQuantity), 0) FROM Ticket t " +
            "WHERE EXISTS ( " +
            " SELECT 1 FROM Ticket t2 " +
            " INNER JOIN t2.eventSession.event e " +
            " INNER JOIN Attendee a ON a.ticket.id = t2.id " +
            " WHERE t.id = t2.id AND e.appUser.id = :organizerId " +
            "AND (CAST(:startDate AS timestamp) IS NULL OR a.createdAt >= :startDate) " +
            "AND (CAST(:endDate AS timestamp) IS NULL OR a.createdAt <= :endDate) " +
            ")")
    int countTicketSold(@Param("organizerId") Long organizerId,
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COALESCE(SUM(t.invitedQuantity), 0) FROM Ticket t " +
            "WHERE EXISTS ( " +
            " SELECT 1 FROM Ticket t2 " +
            " INNER JOIN t2.eventSession.event e " +
            " INNER JOIN Attendee a ON a.ticket.id = t2.id " +
            " WHERE t.id = t2.id AND e.appUser.id = :organizerId " +
            "AND (CAST(:startDate AS timestamp) IS NULL OR a.createdAt >= :startDate) " +
            "AND (CAST(:endDate AS timestamp) IS NULL OR a.createdAt <= :endDate) " +
            ")")
    int countTicketInvited(@Param("organizerId") Long organizerId,
                           @Param("startDate") LocalDateTime startDate,
                           @Param("endDate") LocalDateTime endDate);

    @Modifying
    @Query("UPDATE Ticket t SET t.soldQuantity = COALESCE(t.soldQuantity, 0) + :quantity " +
            "WHERE t.id = :ticketId AND (t.quantity - COALESCE(t.soldQuantity, 0)) >= :quantity")
    int reserveTickets(@Param("ticketId") Long ticketId, @Param("quantity") int quantity);

    @Modifying
    @Query("UPDATE Ticket t SET t.soldQuantity = t.soldQuantity - :quantity WHERE t.id = :ticketId")
    void releaseTickets(@Param("ticketId") Long ticketId, @Param("quantity") int quantity);



}

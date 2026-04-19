package com.auhpp.event_management.repository;

import com.auhpp.event_management.constant.TicketGiftStatus;
import com.auhpp.event_management.entity.AppUser;
import com.auhpp.event_management.entity.TicketGift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketGiftRepository extends JpaRepository<TicketGift, Long>, JpaSpecificationExecutor<TicketGift> {

    List<TicketGift> findAllByStatusAndExpiredAtBefore(TicketGiftStatus status, LocalDateTime currentDate);

    @Query("SELECT tg FROM TicketGift tg " +
            "WHERE exists ( " +
            "SELECT 1 FROM AttendeeTicketGift atg " +
            "WHERE atg.ticketGift.id = tg.id AND atg.attendee.ticket.eventSession.id = :eventSessionId " +
            "AND atg.attendee.owner IN :users )" +
            "AND (:status IS NULL OR tg.status = :status) ")
    List<TicketGift> findAllByUserInAndEventSession(
            @Param("status") TicketGiftStatus status,
            @Param("users") List<AppUser> users,
            @Param("eventSessionId") Long eventSessionId);

}

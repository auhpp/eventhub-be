package com.auhpp.event_management.repository;

import com.auhpp.event_management.constant.TicketGiftStatus;
import com.auhpp.event_management.entity.AttendeeTicketGift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttendeeTicketGiftRepository extends JpaRepository<AttendeeTicketGift, Long> {
    @Query("SELECT atg FROM AttendeeTicketGift atg " +
            "WHERE atg.attendee.id = :attendeeId " +
            "AND atg.status = :status")
    Optional<AttendeeTicketGift> findByAttendeeIdAndStatus(
            @Param("attendeeId") Long attendeeId,
            @Param("status") TicketGiftStatus status
    );

}

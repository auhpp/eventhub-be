package com.auhpp.event_management.repository;

import com.auhpp.event_management.constant.ActionType;
import com.auhpp.event_management.entity.CheckInLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface CheckInLogRepository extends JpaRepository<CheckInLog, Long> {
    @Query("SELECT c FROM CheckInLog c " +
            "WHERE (:ticketCode IS NULL OR c.attendee.ticketCode = :ticketCode) " +
            "AND (:attendeeId IS NULL OR c.attendee.id = :attendeeId) " +
            "AND (:actionType IS NULL OR c.actionType = :actionType) " +
            "AND (:eventStaffId IS NULL OR c.eventStaff.appUser.id = :eventStaffId)" +
            "AND (CAST(:fromTime AS timestamp) IS NULL OR c.createdAt >= :fromTime) " +
            "AND (CAST(:toTime AS timestamp) IS NULL OR c.createdAt <= :toTime)")
    Page<CheckInLog> filter(
            @Param("ticketCode") String ticketCode,
            @Param("attendeeId") Long attendeeId,
            @Param("actionType") ActionType actionType,
            @Param("eventStaffId") Long eventStaffId,
            @Param("fromTime") LocalDateTime fromTime,
            @Param("toTime") LocalDateTime toTime,
            Pageable pageable
    );
}

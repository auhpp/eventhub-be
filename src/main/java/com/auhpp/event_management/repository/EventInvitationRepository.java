package com.auhpp.event_management.repository;

import com.auhpp.event_management.constant.InvitationStatus;
import com.auhpp.event_management.entity.EventInvitation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventInvitationRepository extends JpaRepository<EventInvitation, Long> {

    Optional<EventInvitation> findByToken(String token);

    @Query("SELECT ei FROM EventInvitation ei " +
            "WHERE  ei.ticket.eventSession.id = :eventSessionId " +
            "AND ei.status IN :statuses")
    Page<EventInvitation> findByEventSessionId(
            @Param("statuses") List<InvitationStatus> statuses,
            Long eventSessionId,
            Pageable pageable);

    List<EventInvitation> findAllByStatusAndExpiredAtBefore(InvitationStatus status, LocalDateTime currentDate);
}

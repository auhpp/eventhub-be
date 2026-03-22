package com.auhpp.event_management.repository;

import com.auhpp.event_management.constant.ResalePostStatus;
import com.auhpp.event_management.entity.ResalePost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResalePostRepository extends JpaRepository<ResalePost, Long> {

    @Query("SELECT DISTINCT rp FROM ResalePost rp " +
            "LEFT JOIN rp.attendees a " +
            "WHERE (:eventSessionId IS NULL OR a.ticket.eventSession.id = :eventSessionId) " +
            "AND (:ticketId IS NULL OR a.ticket.id = :ticketId) " +
            "AND (:hasRetail IS NULL OR rp.hasRetail = :hasRetail) " +
            "AND (:quantity IS NULL OR SIZE(rp.attendees) = :quantity) " +
            "AND (:userId IS NULL OR rp.appUser.id = :userId) " +
            "AND (:statuses IS NULL OR rp.status IN :statuses)")
    Page<ResalePost> filter(
            @Param("eventSessionId") Long eventSessionId,
            @Param("ticketId") Long ticketId,
            @Param("hasRetail") Boolean hasRetail,
            @Param("quantity") Integer quantity,
            @Param("userId") Long userId,
            @Param("statuses") List<ResalePostStatus> statuses,
            Pageable pageable
    );
}

package com.auhpp.event_management.repository;

import com.auhpp.event_management.constant.ResalePostStatus;
import com.auhpp.event_management.entity.ResalePost;
import com.auhpp.event_management.repository.custom.ResaleCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ResalePostRepository extends JpaRepository<ResalePost, Long>, ResaleCustomRepository {

    @Query("SELECT DISTINCT rp FROM ResalePost rp " +
            "LEFT JOIN rp.attendees a " +
            "WHERE (:eventSessionId IS NULL OR a.ticket.eventSession.id = :eventSessionId) " +
            "AND (:eventId IS NULL OR a.ticket.eventSession.event.id = :eventId) " +
            "AND (:ticketId IS NULL OR a.ticket.id = :ticketId) " +
            "AND (:hasRetail IS NULL OR rp.hasRetail = :hasRetail) " +
            "AND (:quantity IS NULL OR SIZE(rp.attendees) = :quantity) " +
            "AND (:userId IS NULL OR rp.appUser.id = :userId) " +
            "AND  (CAST(:name AS string ) IS NULL OR LOWER(rp.appUser.fullName) " +
            "LIKE LOWER(CONCAT('%', CAST(:name AS string), '%'))) " +
            "AND (:email IS NULL OR rp.appUser.email = :email) " +
            "AND (:statuses IS NULL OR rp.status IN :statuses)")
    Page<ResalePost> filter(
            @Param("eventSessionId") Long eventSessionId,
            @Param("eventId") Long eventId,
            @Param("ticketId") Long ticketId,
            @Param("hasRetail") Boolean hasRetail,
            @Param("quantity") Integer quantity,
            @Param("userId") Long userId,
            @Param("name") String name,
            @Param("email") String email,
            @Param("statuses") List<ResalePostStatus> statuses,
            Pageable pageable
    );


    @Query("SELECT COUNT(rp) FROM ResalePost rp " +
            "WHERE (:statuses IS NULL OR rp.status IN :statuses) " +
            "AND (CAST(:startDate AS timestamp) IS NULL OR rp.createdAt >= :startDate) " +
            "AND (CAST(:endDate AS timestamp) IS NULL OR rp.createdAt <= :endDate)")
    Integer countResalePost(
            @Param("statuses") List<ResalePostStatus> statuses,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);


}

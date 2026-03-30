package com.auhpp.event_management.repository;

import com.auhpp.event_management.constant.AttendeeStatus;
import com.auhpp.event_management.constant.SourceType;
import com.auhpp.event_management.entity.AppUser;
import com.auhpp.event_management.entity.Attendee;
import com.auhpp.event_management.repository.custom.AttendeeCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendeeRepository extends JpaRepository<Attendee, Long>, JpaSpecificationExecutor<Attendee>,
        AttendeeCustomRepository {
    Optional<Attendee> findByTicketCode(String ticketCode);

    @Query("SELECT a FROM Attendee a WHERE a.status IN :statuses " +
            "AND (a.booking.appUser.email = :email OR a.owner.email = :email) ")
    Page<Attendee> findAllByStatusAndEmailUser(@Param("statuses") List<AttendeeStatus> statuses,
                                               @Param("email") String email, Pageable pageable);

    @Query("SELECT a FROM Attendee a " +
            "WHERE a.owner.email = :email " +
            "AND a.ticket.eventSession.startDateTime > :currentDate ")
    Page<Attendee> findComingAllByEmailUser(@Param("currentDate") LocalDateTime currentDate,
                                            @Param("email") String email, Pageable pageable);

    @Query("SELECT a FROM Attendee a " +
            "WHERE (a.booking.appUser.email = :email OR a.owner.email = :email) " +
            "AND a.ticket.eventSession.endDateTime < current_timestamp ")
    Page<Attendee> findPastAllByEmailUser(@Param("currentDate") LocalDateTime currentDate,
                                          @Param("email") String email, Pageable pageable);

    @Query("SELECT a FROM Attendee a " +
            "WHERE a.ticket.eventSession.id = :eventSessionId " +
            "AND a.status = :status ")
    Page<Attendee> findAllByEventSessionIdAndStatus(@Param("eventSessionId") Long eventSessionId,
                                                    @Param("status") AttendeeStatus status, Pageable pageable);


    @Query("SELECT a FROM Attendee a " +
            "WHERE a.ticket.eventSession.id = :eventSessionId ")
    Page<Attendee> findAllByEventSessionId(@Param("eventSessionId") Long eventSessionId, Pageable pageable);


    @Query("SELECT a FROM Attendee a " +
            "JOIN a.ticket t " +
            "JOIN t.eventSession es " +
            "WHERE es.event.id = :eventId " +
            "AND  a.status = :status")
    List<Attendee> findAllByEventId(@Param("eventId") Long eventId,
                                    @Param("status") AttendeeStatus status);

    @Query("SELECT a FROM Attendee a " +
            "JOIN a.ticket t " +
            "JOIN t.eventSession es " +
            "WHERE es.id = :eventSessionId " +
            "AND  a.status = :status")
    List<Attendee> findAllByEventSessionId(@Param("eventSessionId") Long eventSessionId,
                                           @Param("status") AttendeeStatus status);

    @Query("SELECT DISTINCT a.owner, a.owner.email as email FROM Attendee a " +
            "WHERE a.ticket.eventSession.id = :eventSessionId " +
            "AND (:status IS NULL OR a.status = :status) ")
    Page<AppUser> findUserByEventSession(@Param("status") AttendeeStatus status,
                                         @Param("eventSessionId") Long eventSessionId,
                                         Pageable pageable);

    @Query("SELECT a FROM Attendee a " +
            "WHERE a.ticket.eventSession.id = :eventSessionId " +
            "AND a.owner IN :users " +
            "AND (:sourceType IS NULL OR a.sourceType = :sourceType) " +
            "ORDER BY a.createdAt DESC ")
    List<Attendee> findAllByUserInAndEventSession(
            @Param("sourceType") SourceType sourceType,
            @Param("users") List<AppUser> users,
            @Param("eventSessionId") Long eventSessionId);

    @Query("SELECT COUNT(a) > 0 FROM Attendee  a " +
            "WHERE a.owner.id = :appUserId " +
            "AND a.ticket.eventSession.id = :eventSessionId")
    boolean existsByAppUserIdAndEventSessionId(@Param("appUserId") Long appUserId,
                                               @Param("eventSessionId") Long eventSessionId);

    @Query("SELECT COALESCE(SUM(a.price * a.ticket.eventSession.event.commissionRate + " +
            "a.ticket.eventSession.event.commissionFixedPerTicket), 0) FROM Attendee a " +
            "WHERE a.sourceType IN :sourceTypes " +
            "AND a.booking.resalePost IS NULL " +
            "AND (:eventSessionId IS NULL OR a.ticket.eventSession.id = :eventSessionId) " +
            "AND (CAST(:startDate AS timestamp) IS NULL OR a.createdAt >= :startDate) " +
            "AND (CAST(:endDate AS timestamp) IS NULL OR a.createdAt <= :endDate)")
    Double getCommissionFromEvents(@Param("eventSessionId") Long eventSessionId,
                                   @Param("sourceTypes") List<SourceType> sourceTypes,
                                   @Param("startDate") LocalDateTime startDate,
                                   @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COALESCE(SUM(a.price * (a.booking.resalePost.commissionRate / 100)), 0) FROM Attendee a " +
            "WHERE a.sourceType IN :sourceTypes " +
            "AND a.booking.resalePost IS NOT NULL " +
            "AND (:eventSessionId IS NULL OR a.ticket.eventSession.id = :eventSessionId) " +
            "AND (CAST(:startDate AS timestamp) IS NULL OR a.createdAt >= :startDate) " +
            "AND (CAST(:endDate AS timestamp) IS NULL OR a.createdAt <= :endDate)")
    Double getCommissionFromResales(@Param("eventSessionId") Long eventSessionId,
                                    @Param("sourceTypes") List<SourceType> sourceTypes,
                                    @Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate);
}

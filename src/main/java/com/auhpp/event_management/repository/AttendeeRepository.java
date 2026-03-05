package com.auhpp.event_management.repository;

import com.auhpp.event_management.constant.AttendeeStatus;
import com.auhpp.event_management.constant.SourceType;
import com.auhpp.event_management.entity.AppUser;
import com.auhpp.event_management.entity.Attendee;
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
public interface AttendeeRepository extends JpaRepository<Attendee, Long>, JpaSpecificationExecutor<Attendee> {
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


}

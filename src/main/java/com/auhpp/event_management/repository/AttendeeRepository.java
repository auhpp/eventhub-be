package com.auhpp.event_management.repository;

import com.auhpp.event_management.constant.AttendeeStatus;
import com.auhpp.event_management.constant.AttendeeType;
import com.auhpp.event_management.constant.SourceType;
import com.auhpp.event_management.dto.response.AttendeeCheckedIn;
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
            "AND (:types IS NULL OR a.type IN :types) " +
            "AND (:statuses IS NULL OR a.status IN :statuses) " +
            "AND (:email IS NULL OR a.owner.email = :email)" +
            "AND (:ticketId IS NULL OR a.ticket.id = :ticketId)" +
            "AND (CAST(:name AS string ) IS NULL OR LOWER(a.owner.fullName)" +
            " LIKE LOWER(CONCAT('%', CAST(:name AS string), '%'))) " +
            "")
    Page<AppUser> findUserByEventSession(
            @Param("ticketId") Long ticketId,
            @Param("name") String name,
            @Param("email") String email,
            @Param("types") List<AttendeeType> types,
            @Param("statuses") List<AttendeeStatus> statuses,
            @Param("eventSessionId") Long eventSessionId,
            Pageable pageable);

    @Query("SELECT a FROM Attendee a " +
            "WHERE a.ticket.eventSession.id = :eventSessionId " +
            "AND a.owner IN :users " +
            "AND (:sourceType IS NULL OR a.sourceType = :sourceType) " +
            "AND (:types IS NULL OR a.type IN :types) " +
            "AND (:ticketId IS NULL OR a.ticket.id = :ticketId) " +
            "ORDER BY a.createdAt DESC ")
    List<Attendee> findAllByUserInAndEventSession(
            @Param("ticketId") Long ticketId,
            @Param("types") List<AttendeeType> types,
            @Param("sourceType") SourceType sourceType,
            @Param("users") List<AppUser> users,
            @Param("eventSessionId") Long eventSessionId);

    @Query("SELECT COUNT(a) > 0 FROM Attendee  a " +
            "WHERE a.owner.id = :appUserId " +
            "AND a.ticket.eventSession.id = :eventSessionId")
    boolean existsByAppUserIdAndEventSessionId(@Param("appUserId") Long appUserId,
                                               @Param("eventSessionId") Long eventSessionId);

    @Query("SELECT COALESCE(SUM(a.finalPrice * (e.commissionRate / 100.0) + e.commissionFixedPerTicket), 0.0) " +
            "FROM Attendee a " +
            "JOIN a.ticket t " +
            "JOIN t.eventSession es " +
            "JOIN es.event e " +
            "LEFT JOIN e.eventSeries series " +
            "JOIN a.booking b " +
            "WHERE a.sourceType IN :sourceTypes " +
            "AND b.type = 'BUY' " +
            "AND (:organizerId IS NULL OR es.event.appUser.id = :organizerId) " +
            "AND (:eventSessionId IS NULL OR es.id = :eventSessionId) " +
            "AND (:eventSeriesId IS NULL OR series.id = :eventSeriesId) " +
            "AND (CAST(:startDate AS timestamp) IS NULL OR a.createdAt >= :startDate) " +
            "AND (CAST(:endDate AS timestamp) IS NULL OR a.createdAt <= :endDate)")
    Double getCommissionFromEvents(
            @Param("organizerId") Long organizerId,
            @Param("eventSessionId") Long eventSessionId,
            @Param("eventSeriesId") Long eventSeriesId,
            @Param("sourceTypes") List<SourceType> sourceTypes,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COALESCE(SUM(a.finalPrice * (rp.commissionRate / 100.0)), 0.0) " +
            "FROM Attendee a " +
            "JOIN a.ticket t " +
            "JOIN t.eventSession es " +
            "JOIN es.event e " +
            "LEFT JOIN e.eventSeries series " +
            "JOIN a.booking b " +
            "JOIN b.resalePost rp " +
            "WHERE a.sourceType IN :sourceTypes " +
            "AND (:eventSessionId IS NULL OR es.id = :eventSessionId) " +
            "AND (:eventSeriesId IS NULL OR series.id = :eventSeriesId) " +
            "AND (CAST(:startDate AS timestamp) IS NULL OR a.createdAt >= :startDate) " +
            "AND (CAST(:endDate AS timestamp) IS NULL OR a.createdAt <= :endDate)")
    Double getCommissionFromResales(@Param("eventSessionId") Long eventSessionId,
                                    @Param("eventSeriesId") Long eventSeriesId,
                                    @Param("sourceTypes") List<SourceType> sourceTypes,
                                    @Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COALESCE(COUNT(a.id), 0) FROM Attendee a " +
            "WHERE EXISTS ( " +
            " SELECT 1 FROM Attendee a2 " +
            " LEFT JOIN a2.ticket.eventSession.event e" +
            " WHERE e.appUser.id = :organizerId " +
            " AND a2.type = :type " +
            " AND a2.status = 'CHECKED_IN'" +
            " AND (CAST(:startDate AS timestamp) IS NULL OR a2.createdAt >= :startDate) " +
            " AND (CAST(:endDate AS timestamp) IS NULL OR a2.createdAt <= :endDate)" +
            ")")
    int countTicketCheckIn(@Param("type") AttendeeType type,
                           @Param("organizerId") Long organizerId,
                           @Param("startDate") LocalDateTime startDate,
                           @Param("endDate") LocalDateTime endDate);

    @Query("SELECT a FROM Attendee a " +
            "WHERE a.ticket.eventSession.id = :sessionId " +
            "AND a.owner.email IN :emails " +
            "AND a.status = 'VALID'")
    List<Attendee> findBySessionIdAndEmailsIn(@Param("sessionId") Long sessionId,
                                              @Param("emails") List<String> emails);

    @Query("SELECT a.owner.id as userId, a.owner.email as email," +
            " a.owner.fullName as fullName, a.owner.avatar as avatar," +
            " COUNT(DISTINCT a.id) as checkedInCount " +
            "FROM Attendee a " +
            "WHERE a.ticket.id = :ticketId " +
            "AND a.status = 'CHECKED_IN' " +
            "AND (:email IS NULL OR a.owner.email = :email) " +
            "GROUP BY a.owner.id , a.owner.email, a.owner.fullName, a.owner.avatar ")
    Page<AttendeeCheckedIn> findAllCheckedInByTicketId(@Param("ticketId") Long ticketId,
                                                       @Param("email") String email,
                                                       Pageable pageable);

    @Query("SELECT a FROM Attendee a " +
            "WHERE a.ticket.eventSession.id = :eventSessionId " +
            "AND (:statuses IS NULL OR a.status IN :statuses) " +
            "AND (:types IS NULL OR a.type IN :types) " +
            "AND (:ticketId IS NULL OR a.ticket.id = :ticketId) " +
            "AND (CAST(:name AS string ) IS NULL OR LOWER(a.owner.fullName)" +
            " LIKE LOWER(CONCAT('%', CAST(:name AS string), '%'))) " +
            "AND (:email IS NULL OR a.owner.email = :email) " +
            "AND (:ticketId IS NULL OR a.ticket.id = :ticketId) ")
    Page<Attendee> filterReport(
            @Param("name") String name,
            @Param("email") String email,
            @Param("ticketId") Long ticketId,
            @Param("types") List<AttendeeType> types,
            @Param("statuses") List<AttendeeStatus> statuses,
            @Param("eventSessionId") Long eventSessionId,
            Pageable pageable);

    @Query("SELECT COUNT(a.id) FROM Attendee a " +
            "WHERE a.ticket.id = :ticketId " +
            "AND a.owner.id = :userId " +
            "AND (a.type =  'BUY' OR a.type = 'RESALE' OR a.type = 'GIFT')")
    int countBoughtTicket(@Param("ticketId") Long ticketId, @Param("userId") Long userId);
}

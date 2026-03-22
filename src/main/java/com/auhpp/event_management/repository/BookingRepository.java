package com.auhpp.event_management.repository;

import com.auhpp.event_management.constant.AttendeeType;
import com.auhpp.event_management.constant.BookingStatus;
import com.auhpp.event_management.constant.WalletType;
import com.auhpp.event_management.entity.AppUser;
import com.auhpp.event_management.entity.Booking;
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
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByTransactionIdAndWalletType(String transactionId, WalletType walletType);

    @Query("SELECT DISTINCT b FROM Booking b " +
            "JOIN b.attendees a " +
            "WHERE (:eventSessionId IS NULL OR a.ticket.eventSession.id = :eventSessionId) " +
            "AND (:status IS NULL OR b.status = :status) " +
            "AND (:userId IS NULL OR  a.owner.id = :userId) " +
            "AND (:upcoming IS NULL " +
            "   OR (:upcoming = true AND a.ticket.eventSession.startDateTime >= current_timestamp )" +
            "   OR (:upcoming = false AND a.ticket.eventSession.startDateTime < current_timestamp ))")
    Page<Booking> filterAll(
            @Param("userId") Long userId,
            @Param("eventSessionId") Long eventSessionId,
            @Param("status") BookingStatus status,
            @Param("upcoming") Boolean upcoming,
            Pageable pageable);

    List<Booking> findAllByStatusAndExpiredAtBefore(BookingStatus status, LocalDateTime currentDateTime);

    @Query("SELECT DISTINCT b FROM Booking b " +
            "JOIN b.attendees a " +
            "WHERE b.appUser.email = :email " +
            "AND b.status = :status " +
            "AND a.ticket.eventSession.id = :eventSessionId")
    Optional<Booking> findByEventSessionIdAndCurrentUserAndStatus(@Param("eventSessionId") Long eventSessionId,
                                                                  @Param("email") String email,
                                                                  @Param("status") BookingStatus status);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.attendees a " +
            "WHERE a.ticket.eventSession.id = :eventSessionId " +
            "AND b.appUser IN :users " +
            "ORDER BY b.createdAt ASC ")
    List<Booking> findAllByUserInAndEventSession(@Param("users") List<AppUser> users,
                                                 @Param("eventSessionId") Long eventSessionId);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.attendees a " +
            "JOIN a.ticket t " +
            "JOIN t.eventSession es " +
            "WHERE es.event.id = :eventId " +
            "AND  b.status = :status " +
            "AND (:type IS NULL OR b.type = :type)")
    List<Booking> findAllByEventId(@Param("eventId") Long eventId,
                                   @Param("status") BookingStatus status,
                                   @Param("type") AttendeeType type);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.attendees a " +
            "JOIN a.ticket t " +
            "JOIN t.eventSession es " +
            "WHERE es.id = :eventSessionId " +
            "AND  b.status = :status")
    List<Booking> findAllByEventSessionId(@Param("eventSessionId") Long eventSessionId,
                                          @Param("status") BookingStatus status);

    @Query("SELECT DISTINCT b FROM Booking b " +
            "JOIN b.attendees a " +
            "WHERE b.appUser.email = :email " +
            "AND b.status = :status " +
            "AND b.resalePost.id = :resalePostId")
    Optional<Booking> findByResalePostIdAndCurrentUserAndStatus(@Param("resalePostId") Long resalePostId,
                                                                @Param("email") String email,
                                                                @Param("status") BookingStatus status);
}

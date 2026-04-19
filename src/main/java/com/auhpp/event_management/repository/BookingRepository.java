package com.auhpp.event_management.repository;

import com.auhpp.event_management.constant.AttendeeType;
import com.auhpp.event_management.constant.BookingStatus;
import com.auhpp.event_management.constant.BookingType;
import com.auhpp.event_management.constant.WalletType;
import com.auhpp.event_management.entity.AppUser;
import com.auhpp.event_management.entity.Booking;
import com.auhpp.event_management.repository.custom.BookingCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, BookingCustomRepository {
    Optional<Booking> findByTransactionIdAndWalletType(String transactionId, WalletType walletType);

    @Query("SELECT DISTINCT b FROM Booking b " +
            "JOIN b.attendees a " +
            "WHERE (:eventSessionId IS NULL OR a.ticket.eventSession.id = :eventSessionId) " +
            "AND (:status IS NULL OR b.status = :status) " +
            "AND (:userId IS NULL OR  a.owner.id = :userId) " +
            "AND (:bookingId IS NULL OR b.id = :bookingId) " +
            "AND (:email IS NULL OR b.appUser.email = :email) " +
            "AND (:types IS NULL OR b.type IN :types) " +
            "AND (:upcoming IS NULL " +
            "   OR (:upcoming = true AND a.ticket.eventSession.startDateTime >= current_timestamp )" +
            "   OR (:upcoming = false AND a.ticket.eventSession.startDateTime < current_timestamp ))" +
            "AND (CAST(:startDate AS timestamp) IS NULL OR b.createdAt >= :startDate) " +
            "AND (CAST(:endDate AS timestamp) IS NULL OR b.createdAt <= :endDate) " +
            "")
    Page<Booking> filterAll(
            @Param("bookingId") Long bookingId,
            @Param("email") String email,
            @Param("userId") Long userId,
            @Param("eventSessionId") Long eventSessionId,
            @Param("status") BookingStatus status,
            @Param("upcoming") Boolean upcoming,
            @Param("types") List<BookingType> types,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
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
            "WHERE exists (" +
            "SELECT 1 FROM Attendee a " +
            "WHERE a.booking.id = b.id AND a.ticket.eventSession.id = :eventSessionId )" +
            "AND b.appUser IN :users " +
            "AND b.type IN :booingTypes " +
            "ORDER BY b.createdAt ASC ")
    List<Booking> findAllByUserInAndEventSession(
            @Param("booingTypes") List<BookingType> booingTypes,
            @Param("users") List<AppUser> users,
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

    @Query("SELECT COALESCE(SUM(b.totalAmount), 0.0) FROM Booking b " +
            "WHERE (:bookingType IS NULL OR b.type = :bookingType) " +
            "AND (CAST(:startDate AS timestamp) IS NULL OR b.createdAt >= :startDate) " +
            "AND (CAST(:endDate AS timestamp) IS NULL OR b.createdAt <= :endDate) " +

            // 1. Lọc độc lập theo Session
            "AND (:eventSessionId IS NULL OR EXISTS ( " +
            "   SELECT 1 FROM Attendee a JOIN a.ticket t JOIN t.eventSession es " +
            "   WHERE a.booking.id = b.id AND es.id = :eventSessionId )) " +

            // 2. Lọc độc lập theo Series (Dùng LEFT JOIN để an toàn)
            "AND (:eventSeries IS NULL OR EXISTS ( " +
            "   SELECT 1 FROM Attendee a JOIN a.ticket t JOIN t.eventSession es JOIN es.event e LEFT JOIN e.eventSeries series " +
            "   WHERE a.booking.id = b.id AND series.id = :eventSeries )) " +

            // 3. Lọc độc lập theo Organizer
            "AND (:organizerId IS NULL OR EXISTS ( " +
            "   SELECT 1 FROM Attendee a JOIN a.ticket t JOIN t.eventSession es JOIN es.event e " +
            "   WHERE a.booking.id = b.id AND e.appUser.id = :organizerId ))")
    Double getTotalRevenue(
            @Param("eventSeries") Long eventSeries,
            @Param("organizerId") Long organizerId,
            @Param("eventSessionId") Long eventSessionId,
            @Param("bookingType") BookingType bookingType,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COALESCE(SUM(b.finalAmount), 0.0) FROM Booking b " +
            "WHERE (:bookingType IS NULL OR b.type = :bookingType) " +
            "AND (CAST(:startDate AS timestamp) IS NULL OR b.createdAt >= :startDate) " +
            "AND (CAST(:endDate AS timestamp) IS NULL OR b.createdAt <= :endDate) " +
            "AND (:eventSessionId IS NULL OR EXISTS ( " +
            "   SELECT 1 FROM Attendee a JOIN a.ticket t JOIN t.eventSession es " +
            "   WHERE a.booking.id = b.id AND es.id = :eventSessionId )) " +
            "AND (:eventSeriesId IS NULL OR EXISTS ( " +
            "   SELECT 1 FROM Attendee a JOIN a.ticket t JOIN t.eventSession es JOIN es.event e LEFT JOIN e.eventSeries series " +
            "   WHERE a.booking.id = b.id AND series.id = :eventSeriesId )) " +
            "AND (:organizerId IS NULL OR EXISTS ( " +
            "   SELECT 1 FROM Attendee a JOIN a.ticket t JOIN t.eventSession es JOIN es.event e " +
            "   WHERE a.booking.id = b.id AND e.appUser.id = :organizerId ))")
    Double getVoucherRevenue(
            @Param("organizerId") Long organizerId,
            @Param("eventSeriesId") Long eventSeriesId,
            @Param("eventSessionId") Long eventSessionId,
            @Param("bookingType") BookingType bookingType,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Modifying
    @Query("UPDATE Booking b SET b.status = 'CANCELLED' WHERE b.id = :id AND b.status = 'PENDING'")
    int lockBookingForDeletion(@Param("id") Long id);

    @Query("SELECT DISTINCT b FROM Booking b " +
            "JOIN b.attendees a " +
            "WHERE b.status = 'PAID' " +
            "AND (b.isFundReleased IS NULL OR b.isFundReleased = false) " +
            "AND a.ticket.eventSession.id = :sessionId")
    List<Booking> findBookingsReadyForFundReleaseBySession(@Param("sessionId") Long sessionId);

}

package com.auhpp.event_management.repository;

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

    @Query("SELECT b FROM Booking b " +
            "WHERE b.appUser.email = :email AND b.status = :status ")
    Page<Booking> findAllByEmailUserAndStatus(String email, BookingStatus status, Pageable pageable);

    List<Booking> findAllByStatusAndExpiredAtBefore(BookingStatus status, LocalDateTime currentDateTime);

    @Query("SELECT DISTINCT b FROM Booking b " +
            "JOIN b.attendees a " +
            "WHERE b.appUser.email = :email " +
            "AND b.status = :status " +
            "AND a.ticket.eventSession.id = :eventSessionId")
    Optional<Booking> findByEventSessionIdAndCurrentUserAndStatus(Long eventSessionId,
                                                                  String email, BookingStatus status);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.appUser.email = :email ")
    Page<Booking> findAllByEmailUser(String email, Pageable pageable);

    @Query("SELECT DISTINCT b.appUser, b.createdAt as bookingCreatedAt FROM Booking b " +
            "JOIN b.attendees a " +
            "WHERE a.ticket.eventSession.id = :eventSessionId " +
            "AND b.status IN :statuses ")
    Page<AppUser> findUserByEventSession(@Param("statuses") List<BookingStatus> statuses, Long eventSessionId,
                                         Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.attendees a " +
            "WHERE a.ticket.eventSession.id = :eventSessionId " +
            "AND b.appUser IN :users " +
            "ORDER BY b.createdAt ASC ")
    List<Booking> findAllByUserInAndEventSession(@Param("users") List<AppUser> users, Long eventSessionId);
}

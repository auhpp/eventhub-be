package com.auhpp.event_management.repository;

import com.auhpp.event_management.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    @Query("SELECT c FROM Coupon c " +
            "JOIN TicketCoupon tc ON tc.coupon.id = c.id " +
            "WHERE tc.ticket.eventSession.event.id = :eventId " +
            "AND c.code = :code")
    Optional<Coupon> findByCodeAndEventId(@Param("code") String code, @Param("eventId") Long eventId);


    @Query("SELECT c FROM Coupon c " +
            "JOIN TicketCoupon tc ON tc.coupon.id = c.id " +
            "WHERE (:eventId IS NULL OR tc.ticket.eventSession.event.id = :eventId) ")
    Page<Coupon> filterCoupon(@Param("eventId") Long eventId, Pageable pageable);
}

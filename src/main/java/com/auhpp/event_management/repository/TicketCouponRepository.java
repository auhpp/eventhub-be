package com.auhpp.event_management.repository;

import com.auhpp.event_management.entity.TicketCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketCouponRepository extends JpaRepository<TicketCoupon, Long> {
    Optional<TicketCoupon> findByTicketIdAndCouponId(Long ticketId, Long couponId);
}

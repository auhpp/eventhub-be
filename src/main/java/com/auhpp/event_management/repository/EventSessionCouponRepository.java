package com.auhpp.event_management.repository;

import com.auhpp.event_management.entity.TicketCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventSessionCouponRepository extends JpaRepository<TicketCoupon, Long> {
}

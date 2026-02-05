package com.auhpp.event_management.repository;

import com.auhpp.event_management.constant.TicketGiftStatus;
import com.auhpp.event_management.entity.TicketGift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketGiftRepository extends JpaRepository<TicketGift, Long>, JpaSpecificationExecutor<TicketGift> {

    List<TicketGift> findAllByStatusAndExpiredAtBefore(TicketGiftStatus status, LocalDateTime currentDate);
}

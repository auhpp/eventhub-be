package com.auhpp.event_management.repository;

import com.auhpp.event_management.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Ticket t SET t.soldQuantity = :quantity WHERE t.id = :id")
    void updateSoldQuantity(@Param("id") Long id, @Param("quantity") int quantity);
}

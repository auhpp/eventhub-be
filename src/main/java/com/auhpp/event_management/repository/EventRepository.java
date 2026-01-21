package com.auhpp.event_management.repository;

import com.auhpp.event_management.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT e FROM Event e WHERE e.appUser.id = :userId")
    Page<Event> findAllByUserId(@Param("userId") Long userId, Pageable pageable);
}

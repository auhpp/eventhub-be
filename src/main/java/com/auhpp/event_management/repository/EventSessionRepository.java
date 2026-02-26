package com.auhpp.event_management.repository;

import com.auhpp.event_management.entity.EventSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventSessionRepository extends JpaRepository<EventSession, Long> {
}

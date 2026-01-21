package com.auhpp.event_management.repository;

import com.auhpp.event_management.entity.EventStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventStaffRepository extends JpaRepository<EventStaff, Long> {
}

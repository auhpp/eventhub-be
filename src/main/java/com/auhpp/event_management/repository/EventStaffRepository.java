package com.auhpp.event_management.repository;

import com.auhpp.event_management.entity.EventStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventStaffRepository extends JpaRepository<EventStaff, Long>,
        JpaSpecificationExecutor<EventStaff> {
    Optional<EventStaff> findByToken(String token);

    Optional<EventStaff> findByEmailAndEventId(String email, Long id);
}

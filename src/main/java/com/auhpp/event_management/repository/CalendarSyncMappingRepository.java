package com.auhpp.event_management.repository;

import com.auhpp.event_management.entity.CalendarSyncMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalendarSyncMappingRepository extends JpaRepository<CalendarSyncMapping, Long> {
    List<CalendarSyncMapping> findByEventSessionIdAndGoogleEventIdIsNotNull(Long eventSessionId);
}

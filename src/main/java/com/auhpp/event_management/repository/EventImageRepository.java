package com.auhpp.event_management.repository;

import com.auhpp.event_management.constant.ProcessStatus;
import com.auhpp.event_management.entity.EventImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventImageRepository extends JpaRepository<EventImage, Long> {

    @Query("SELECT ei FROM EventImage ei " +
            "WHERE (:processStatus IS NULL OR ei.processStatus = :processStatus) " +
            "AND ei.event.id = :eventId ")
    Page<EventImage> findByProcessStatusAndEventId(@Param("processStatus") ProcessStatus processStatus,
                                                   @Param("eventId") Long eventId,
                                                   Pageable pageable);
}

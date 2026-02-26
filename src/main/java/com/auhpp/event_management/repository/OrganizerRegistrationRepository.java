package com.auhpp.event_management.repository;

import com.auhpp.event_management.entity.OrganizerRegistration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizerRegistrationRepository extends JpaRepository<OrganizerRegistration, Long> {

    @Query("SELECT or FROM OrganizerRegistration or WHERE (:userId IS NULL OR or.appUser.id = :userId)")
    Page<OrganizerRegistration> findAllByUserId(@Param("userId") Long userId, Pageable pageable);
}

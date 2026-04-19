package com.auhpp.event_management.repository;

import com.auhpp.event_management.constant.RegistrationStatus;
import com.auhpp.event_management.entity.OrganizerRegistration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface OrganizerRegistrationRepository extends JpaRepository<OrganizerRegistration, Long> {

    @Query("SELECT or FROM OrganizerRegistration or " +
            "WHERE (:userId IS NULL OR or.appUser.id = :userId) " +
            "AND  (:userEmail IS NULL OR or.appUser.email = :userEmail) " +
            "AND (CAST(:organizerName AS string ) IS NULL OR LOWER(or.businessName)" +
            " LIKE LOWER(CONCAT('%', CAST(:organizerName AS string), '%'))) " +
            "AND  (:status IS NULL OR or.status = :status) " +
            "AND (CAST(:startDate AS timestamp) IS NULL OR or.createdAt >= :startDate) " +
            "AND (CAST(:endDate AS timestamp) IS NULL OR or.createdAt <= :endDate)")
    Page<OrganizerRegistration> filter(@Param("userId") Long userId,
                                       @Param("userEmail") String userEmail,
                                       @Param("organizerName") String organizerName,
                                       @Param("status") RegistrationStatus status,
                                       @Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate,
                                       Pageable pageable);

    Integer countByStatus(RegistrationStatus status);

}

package com.auhpp.event_management.repository;

import com.auhpp.event_management.constant.WithdrawalStatus;
import com.auhpp.event_management.entity.WithdrawalRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WithdrawalRequestRepository extends JpaRepository<WithdrawalRequest, Long> {

    @Query("SELECT wr FROM WithdrawalRequest wr " +
            "WHERE (:userEmail IS NULL OR wr.wallet.appUser.email = :userEmail) " +
            "AND (:statuses IS NULL OR wr.status IN :statuses)" +
            "AND (CAST(:startDate AS timestamp) IS NULL OR wr.createdAt >= :startDate) " +
            "AND (CAST(:endDate AS timestamp) IS NULL OR wr.createdAt <= :endDate)")
    Page<WithdrawalRequest> filter(@Param("userEmail") String userEmail,
                                   @Param("statuses") List<WithdrawalStatus> statuses,
                                   @Param("startDate") LocalDateTime startDate,
                                   @Param("endDate") LocalDateTime endDate,
                                   Pageable pageable);
}

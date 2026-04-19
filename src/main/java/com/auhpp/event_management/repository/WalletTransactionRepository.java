package com.auhpp.event_management.repository;

import com.auhpp.event_management.constant.TransactionType;
import com.auhpp.event_management.entity.WalletTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {
    @Query("SELECT wt FROM WalletTransaction wt " +
            "WHERE wt.wallet.id = :walletId " +
            "AND (:transactionTypes IS NULL OR wt.transactionType IN :transactionTypes) ")
    Page<WalletTransaction> filter(@Param("walletId") Long walletId,
                                   @Param("transactionTypes") List<TransactionType> transactionTypes,
                                   Pageable pageable);
}

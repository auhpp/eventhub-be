package com.auhpp.event_management.repository;

import com.auhpp.event_management.constant.WalletStatus;
import com.auhpp.event_management.constant.WalletType;
import com.auhpp.event_management.entity.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    @Query("SELECT w FROM Wallet w " +
            "WHERE (:email IS NULL OR w.appUser.email = :email) " +
            "AND (:type IS NULL OR w.type = :type) " +
            "AND (:status IS NULL OR w.status = :status) " +
            "AND (:userId IS NULL OR w.appUser.id = :userId)")
    Page<Wallet> filter(
            @Param("userId") Long userId,
            @Param("email") String email,
            @Param("type") WalletType type,
            @Param("status") WalletStatus status,
            Pageable pageable);
}

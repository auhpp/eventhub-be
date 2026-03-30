package com.auhpp.event_management.repository;

import com.auhpp.event_management.constant.RoleName;
import com.auhpp.event_management.entity.AppUser;
import com.auhpp.event_management.repository.custom.AppUserCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long>, AppUserCustomRepository {
    Optional<AppUser> findByEmail(String email);

    @Query("SELECT u FROM AppUser u " +
            "WHERE (:email IS NULL OR u.email = :email)" +
            "AND (:status IS NULL OR u.status = :status)" +
            "AND (:roleName IS NULL OR u.role.name = :roleName)")
    Page<AppUser> filter(@Param("email") String email,
                         @Param("status") Boolean status,
                         @Param("roleName") RoleName roleName,
                         Pageable pageable
    );

    @Query("SELECT COUNT(u) FROM AppUser u " +
            "WHERE (:statuses IS NULL OR u.status IN :statuses) " +
            "AND (CAST(:startDate AS timestamp) IS NULL OR u.createdAt >= :startDate) " +
            "AND (CAST(:endDate AS timestamp) IS NULL OR u.createdAt <= :endDate)")
    Integer countUser(
            @Param("statuses") List<Boolean> statuses,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}

package com.auhpp.event_management.repository;

import com.auhpp.event_management.entity.CategoryFollower;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryFollowerRepository extends JpaRepository<CategoryFollower, Long> {
    @Query("SELECT cf from CategoryFollower cf " +
            "WHERE (:userId IS NULL OR cf.appUser.id = :userId) " +
            "AND (:categoryId IS NULL OR cf.category.id = :categoryId) ")
    Page<CategoryFollower> filterCategoryFollower(@Param("userId") Long userId,
                                                  @Param("categoryId") Long categoryId,
                                                  Pageable pageable);

    Optional<CategoryFollower> findByAppUserIdAndCategoryId(
            @Param("userId") Long userId,
            @Param("categoryId") Long categoryId
    );

    @Query("SELECT COUNT(cf) from CategoryFollower cf " +
            "WHERE (:userId IS NULL OR cf.appUser.id = :userId) " +
            "AND (:categoryId IS NULL OR cf.category.id = :categoryId) ")
    Integer countCategoryFollower(
            @Param("userId") Long userId,
            @Param("categoryId") Long categoryId
    );
}

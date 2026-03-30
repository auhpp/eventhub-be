package com.auhpp.event_management.repository;

import com.auhpp.event_management.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c, COUNT(DISTINCT cf.id) as followerCount," +
            " COUNT(DISTINCT e.id) as eventCount" +
            " FROM Category c " +
            "LEFT JOIN c.categoryFollowers cf " +
            "LEFT JOIN c.events e " +
            "WHERE (CAST(:name AS string ) IS NULL OR LOWER(c.name) " +
            "LIKE LOWER(CONCAT('%', CAST(:name AS string), '%'))) " +
            "GROUP BY c.id ")
    Page<Object[]> filter(@Param("name") String name,
                          Pageable pageable);
}

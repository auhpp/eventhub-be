package com.auhpp.event_management.repository;

import com.auhpp.event_management.constant.TagType;
import com.auhpp.event_management.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query("SELECT t FROM Tag t " +
            "WHERE (:type IS NULL OR t.type = :type) " +
            "AND (CAST(:name AS string ) IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', CAST(:name AS string), '%'))) ")
    Page<Tag> filter(@Param("type") TagType type, @Param("name") String name, Pageable pageable);

    @Query("SELECT t FROM Tag t " +
            "WHERE (:type IS NULL OR t.type = :type) " +
            "AND (:userId IS NULL OR t.id IN (" +
            "   SELECT ut.tag.id FROM UserTag ut " +
            "   WHERE ut.appUser.id = :userId))" +
            "AND (CAST(:name AS string ) IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', CAST(:name AS string), '%'))) ")
    List<Tag> findAllByType(@Param("type") TagType type, @Param("name") String name,
                            @Param("userId") Long userId);

    Optional<Tag> findBySlug(String slug);
}

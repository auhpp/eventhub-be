package com.auhpp.event_management.repository;

import com.auhpp.event_management.entity.UserNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNoteRepository extends JpaRepository<UserNote, Long> {

    @Query("SELECT un FROM UserNote un " +
            "WHERE un.appUser.id = :userId " +
            "AND (:questionId IS NULL OR un.question.id = :questionId)")
    Page<UserNote> filter(@Param("userId") Long userId, @Param("questionId") Long questionId, Pageable pageable);
}

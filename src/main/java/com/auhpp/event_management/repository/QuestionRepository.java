package com.auhpp.event_management.repository;

import com.auhpp.event_management.constant.QuestionStatus;
import com.auhpp.event_management.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query("SELECT q FROM Question q " +
            "WHERE (:eventSessionId IS NULL OR q.eventSession.id = :eventSessionId) " +
            "AND (:userId IS NULL OR q.appUser.id = :userId) " +
            "AND (:statuses IS NULL OR q.status IN :statuses) " +
            "ORDER BY q.hasPin DESC, SIZE(q.upvoteQuestions) DESC, q.createdAt DESC ")
    Page<Question> filter(@Param("eventSessionId") Long eventSessionId,
                          @Param("userId") Long userId,
                          @Param("statuses") List<QuestionStatus> statuses,
                          Pageable pageable);
}

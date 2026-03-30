package com.auhpp.event_management.repository;

import com.auhpp.event_management.entity.UpvoteQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UpvoteQuestionRepository extends JpaRepository<UpvoteQuestion, Long> {
    boolean existsByAppUserIdAndQuestionId(Long appUserId, Long questionId);

    Optional<UpvoteQuestion> deleteByAppUserIdAndQuestionId(Long appUserId, Long questionId);

}

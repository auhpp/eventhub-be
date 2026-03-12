package com.auhpp.event_management.repository;

import com.auhpp.event_management.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r " +
            "WHERE (:eventSessionId IS NULL OR r.eventSession.id = :eventSessionId) " +
            "AND (:userId IS NULL OR r.attendee.owner.id = :userId) " +
            "AND (:attendeeId IS NULL OR r.attendee.id = :attendeeId)")
    Page<Review> filterReview(@Param("eventSessionId") Long eventSessionId,
                              @Param("userId") Long userId,
                              @Param("attendeeId") Long attendeeId,
                              Pageable pageable);

    @Query("SELECT r.rating, COUNT(r.id) FROM Review r " +
            "WHERE r.eventSession.id = :eventSessionId " +
            "GROUP BY r.rating ")
    List<Object[]> countReviewsByRating(@Param("eventSessionId") Long eventSessionId);
}

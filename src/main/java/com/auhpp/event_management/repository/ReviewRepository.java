package com.auhpp.event_management.repository;

import com.auhpp.event_management.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r " +
            "WHERE (:eventSessionId IS NULL OR r.eventSession.id = :eventSessionId) " +
            "AND (:userId IS NULL OR r.attendee.owner.id = :userId) " +
            "AND (:attendeeId IS NULL OR r.attendee.id = :attendeeId)" +
            "AND (:rating IS NULL OR r.rating = :rating)" +
            "AND (:email IS NULL OR r.attendee.owner.email = :email)")
    Page<Review> filterReview(@Param("eventSessionId") Long eventSessionId,
                              @Param("userId") Long userId,
                              @Param("attendeeId") Long attendeeId,
                              @Param("rating") Long rating,
                              @Param("email") String email,
                              Pageable pageable);

    @Query("SELECT r.rating, COALESCE(COUNT(distinct r.id), 0) FROM Review r " +
            "WHERE ( :eventSessionId IS NULL OR  r.eventSession.id = :eventSessionId )" +
            "AND (:organizerId IS NULL OR r.eventSession.event.appUser.id = :organizerId)" +
            "AND (:eventSeriesId IS NULL OR r.eventSession.event.eventSeries.id = :eventSeriesId)" +
            "AND (CAST(:startDate AS timestamp) IS NULL OR r.createdAt >= :startDate) " +
            "AND (CAST(:endDate AS timestamp) IS NULL OR r.createdAt <= :endDate) " +
            "GROUP BY r.rating ")
    List<Object[]> countReviewsByRating(
            @Param("eventSeriesId") Long eventSeriesId,
            @Param("eventSessionId") Long eventSessionId,
            @Param("organizerId") Long organizerId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);


    @Query("SELECT AVG(r.rating), COUNT(r.id) FROM Review r " +
            "WHERE EXISTS (" +
            " SELECT 1 FROM Review r2 " +
            " WHERE r2.id = r.id AND r2.eventSession.event.appUser.id = :organizerId " +
            "AND (CAST(:startDate AS timestamp) IS NULL OR r.createdAt >= :startDate) " +
            "AND (CAST(:endDate AS timestamp) IS NULL OR r.createdAt <= :endDate) " +
            ") ")
    Object[] countAverage(@Param("organizerId") Long organizerId,
                          @Param("startDate") LocalDateTime startDate,
                          @Param("endDate") LocalDateTime endDate);
}

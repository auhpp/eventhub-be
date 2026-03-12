package com.auhpp.event_management.repository;

import com.auhpp.event_management.entity.UserFollower;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserFollowerRepository extends JpaRepository<UserFollower, Long> {
    @Query("SELECT uf from UserFollower uf " +
            "WHERE (:followerId IS NULL OR uf.follower.id = :followerId) " +
            "AND (:followedId IS NULL OR uf.followed.id = :followedId) ")
    Page<UserFollower> filterUserFollower(@Param("followerId") Long followerId,
                                          @Param("followedId") Long followedId,
                                          Pageable pageable);

    Optional<UserFollower> findByFollowerIdAndFollowedId(
            @Param("followerId") Long followerId,
            @Param("followedId") Long followedId
    );

    @Query("SELECT COUNT(uf) from UserFollower uf " +
            "WHERE (:followerId IS NULL OR uf.follower.id = :followerId) " +
            "AND (:followedId IS NULL OR uf.followed.id = :followedId) ")
    Integer countUserFollower(@Param("followerId") Long followerId,
                              @Param("followedId") Long followedId);
}

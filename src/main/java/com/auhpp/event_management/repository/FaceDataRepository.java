package com.auhpp.event_management.repository;

import com.auhpp.event_management.dto.response.FaceSearchResult;
import com.auhpp.event_management.entity.FaceData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaceDataRepository extends JpaRepository<FaceData, Long> {
    @Query(value = "SELECT ei.image_url as imageUrl, " +
            "ei.id as eventImageId, " +
            "(fd.face_encoding <=> cast(:vectorStr as vector)) as distance " +
            "FROM face_data fd " +
            "JOIN event_image ei ON fd.event_image_id = ei.id " +
            "WHERE ei.event_id = :eventId " +
            "AND (fd.face_encoding <=> cast(:vectorStr as vector)) < :threshold " +
            "ORDER BY distance ASC " +
            "LIMIT :limit ",
            nativeQuery = true)
    List<FaceSearchResult> searchFace(
            @Param("vectorStr") String vectorStr,
            @Param("threshold") double threshold,
            @Param("eventId") Long eventId,
            @Param("limit") int limit
    );
}

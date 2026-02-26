package com.auhpp.event_management.entity;

import com.auhpp.event_management.dto.pojo.FaceCoordinate;
import com.auhpp.event_management.entity.converter.VectorFloatListConverter;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.Type;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FaceData {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb", nullable = false)
    private FaceCoordinate coordinate;

    @Column(columnDefinition = "vector", nullable = false)
    @Convert(converter = VectorFloatListConverter.class)
    @ColumnTransformer(write = "?::vector")
    private List<Float> faceEncoding;

    private Double detectionScore;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private EventImage eventImage;
}

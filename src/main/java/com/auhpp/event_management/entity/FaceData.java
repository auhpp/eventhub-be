package com.auhpp.event_management.entity;

import com.auhpp.event_management.entity.converter.VectorFloatListConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;

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

    @Column(columnDefinition = "vector(512)", nullable = false)
    @Convert(converter = VectorFloatListConverter.class)
    @ColumnTransformer(write = "?::vector")
    private List<Float> faceEncoding;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private EventImage eventImage;
}

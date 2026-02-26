package com.auhpp.event_management.dto.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaceCoordinate implements Serializable {
    private Double x1;

    private Double y1;

    private Double x2;

    private Double y2;
}

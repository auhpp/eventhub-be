package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.response.PointResponse;
import org.locationtech.jts.geom.Point;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PointMapper {

    @Mapping(source = "x", target = "longitude")
    @Mapping(source = "y", target = "latitude")
    PointResponse toPointResponse(Point point);
}

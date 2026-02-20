package com.auhpp.event_management.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Converter
public class VectorFloatListConverter implements AttributeConverter<List<Float>, String> {
    @Override
    public String convertToDatabaseColumn(List<Float> attribute) {
        if (attribute == null) return null;
        // convert List<Float> to string format vector Postgres: "[1.1,2.2,3.3]"
        return attribute.toString();
    }

    @Override
    public List<Float> convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        // Parse string "[1.1,2.2]" from DB to List<Float>
        String cleanData = dbData.replace("[", "").replace("]", "");
        if (cleanData.trim().isEmpty()) return List.of();
        return Arrays.stream(cleanData.split(","))
                .map(String::trim)
                .map(Float::parseFloat)
                .collect(Collectors.toList());
    }
}

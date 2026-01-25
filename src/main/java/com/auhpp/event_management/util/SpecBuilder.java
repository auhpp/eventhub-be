package com.auhpp.event_management.util;

import com.auhpp.event_management.repository.specification.BaseSpecification;
import com.auhpp.event_management.repository.specification.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

public class SpecBuilder {
    public static <T> Specification<T> create(
            String key, String operation, Object value, String joinTable
    ) {
        if (value == null) return null;
        if (value instanceof String && ((String) value).isEmpty()) {
            return null;
        }
        return new BaseSpecification<>(
                SearchCriteria.builder()
                        .key(key)
                        .operation(operation)
                        .value(value)
                        .nameTableJoin(joinTable)
                        .build()
        );
    }

    public static <T> Specification<T> create(String key, String operation, Object value) {
        return create(key, operation, value, null);
    }
}



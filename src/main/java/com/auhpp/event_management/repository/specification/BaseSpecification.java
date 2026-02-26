package com.auhpp.event_management.repository.specification;


import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class BaseSpecification<T> implements Specification<T> {
    private SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<T> root,
                                 CriteriaQuery<?> query,
                                 CriteriaBuilder builder) {
        // handle join and normal field
        Path<Object> path;
        if (criteria.getNameTableJoin() != null) {
            path = root.join(criteria.getNameTableJoin(), JoinType.LEFT).get(criteria.getKey());
        } else {
            path = root.get(criteria.getKey());
        }

        // handle operation
        switch (criteria.getOperation().toLowerCase()) {
            case ":": //Like
                return builder.like(builder.lower(path.as(String.class)),
                        "%" + criteria.getValue().toString().toLowerCase() + "%"
                );
            case "=": //equal
                return builder.equal(path, criteria.getValue());
            case "!=":
                return builder.notEqual(path, criteria.getValue());
            case ">": //greater than
                return builder.greaterThanOrEqualTo(path.as(Comparable.class), (Comparable) criteria.getValue());
            case "<": //less than
                return builder.lessThanOrEqualTo(path.as(Comparable.class), (Comparable) criteria.getValue());
            case "in": // IN
                return path.in(criteria.getValue());
            default:
                return null;
        }
    }
}

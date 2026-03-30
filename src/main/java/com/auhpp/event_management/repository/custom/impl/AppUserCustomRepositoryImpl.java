package com.auhpp.event_management.repository.custom.impl;

import com.auhpp.event_management.constant.RoleName;
import com.auhpp.event_management.dto.request.DateRangeFilterRequest;
import com.auhpp.event_management.dto.response.UserGrowthResponse;
import com.auhpp.event_management.repository.custom.AppUserCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AppUserCustomRepositoryImpl implements AppUserCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private void handleJoin(StringBuilder query) {
        query.append(" INNER JOIN role r on r.id = u.role_id ");
    }

    private void handleQuery(DateRangeFilterRequest request, RoleName roleName,
                             StringBuilder where, Map<String, Object> params) {
        if (roleName != null) {
            where.append(" AND r.name = :roleName ");
            params.put("roleName", roleName.name());
        }
        if (request.getStartDate() != null) {
            where.append(" AND u.created_at >= :startDate ");
            params.put("startDate", request.getStartDate());
        }
        if (request.getEndDate() != null) {
            where.append(" AND u.created_at <= :endDate");
            params.put("endDate", request.getEndDate());
        }
    }


    @Override
    public List<UserGrowthResponse> getUserGrowthResponse(DateRangeFilterRequest request, RoleName roleName) {
        StringBuilder query = new StringBuilder(
                "SELECT TO_CHAR(u.created_at, 'MM-YYYY') as timeLabel, COUNT(DISTINCT u.id) as newUsersCount " +
                        " FROM app_user u ");
        handleJoin(query);

        StringBuilder where = new StringBuilder(" WHERE 1 = 1 ");
        Map<String, Object> params = new HashMap<>();
        handleQuery(request, roleName, where, params);
        query.append(where);
        query.append(" GROUP BY TO_CHAR(u.created_at, 'MM-YYYY') ");

        Query dataQuery = entityManager.createNativeQuery(query.toString(), Tuple.class);
        for (String key : params.keySet()) {
            dataQuery.setParameter(key, params.get(key));
        }
        List<Tuple> tuples = dataQuery.getResultList();
        List<UserGrowthResponse> res = tuples.stream().map(
                tuple -> UserGrowthResponse.builder()
                        .timeLabel(tuple.get("timeLabel", String.class))
                        .newUsersCount(tuple.get("newUsersCount") != null ? ((Number) tuple.get("newUsersCount")).longValue() : 0L)
                        .build()
        ).toList();
        return res;
    }
}

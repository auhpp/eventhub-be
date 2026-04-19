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

    private void handleJoin(RoleName roleName, StringBuilder query) {
        if (roleName != null) {
            query.append(" INNER JOIN role r on r.id = u.role_id ");
        }
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
                "SELECT TO_CHAR(u.created_at, 'MM-YYYY') as time_label, COUNT(DISTINCT u.id) as new_users_count " +
                        " FROM app_user u ");
        handleJoin(roleName, query);

        StringBuilder where = new StringBuilder(" WHERE 1 = 1 ");
        Map<String, Object> params = new HashMap<>();
        handleQuery(request, roleName, where, params);
        query.append(where);
        query.append(" GROUP BY TO_CHAR(u.created_at, 'MM-YYYY') ");
        query.append(" ORDER BY MIN(u.created_at) ASC ");

        Query dataQuery = entityManager.createNativeQuery(query.toString(), Tuple.class);
        for (String key : params.keySet()) {
            dataQuery.setParameter(key, params.get(key));
        }
        List<Tuple> tuples = dataQuery.getResultList();
        List<UserGrowthResponse> res = tuples.stream().map(
                tuple -> UserGrowthResponse.builder()
                        .timeLabel(tuple.get("time_label", String.class))
                        .newUsersCount(tuple.get("new_users_count") != null ? ((Number) tuple.get("new_users_count")).longValue() : 0L)
                        .build()
        ).toList();
        return res;
    }
}

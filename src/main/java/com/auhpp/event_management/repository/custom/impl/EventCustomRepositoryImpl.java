package com.auhpp.event_management.repository.custom.impl;

import com.auhpp.event_management.constant.EventStatus;
import com.auhpp.event_management.dto.request.DateRangeFilterRequest;
import com.auhpp.event_management.dto.response.CategoryDistributionResponse;
import com.auhpp.event_management.dto.response.EventApprovalStatResponse;
import com.auhpp.event_management.repository.custom.EventCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EventCustomRepositoryImpl implements EventCustomRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private void handleJoin(StringBuilder query) {
        query.append(" INNER JOIN category c ON e.category_id = c.id ");
    }

    private void handleQuery(EventStatus status,
                             DateRangeFilterRequest dateRangeFilterRequest, StringBuilder where, Map<String, Object> params) {

        if (status != null) {
            where.append(" AND e.status = :status ");
            params.put("status", status.name());
        }
        if (dateRangeFilterRequest.getStartDate() != null) {
            where.append(" AND e.created_at >= :startDate ");
            params.put("startDate", dateRangeFilterRequest.getStartDate());
        }
        if (dateRangeFilterRequest.getEndDate() != null) {
            where.append(" AND e.created_at <= :endDate");
            params.put("endDate", dateRangeFilterRequest.getEndDate());
        }
    }

    @Override
    public List<CategoryDistributionResponse> getCategoryDistribution(
            DateRangeFilterRequest request, EventStatus status) {
        StringBuilder query = new StringBuilder(
                "SELECT c.id as categoryId, c.name as categoryName, COUNT(e.id) as eventCount " +
                        " FROM event e ");
        handleJoin(query);

        StringBuilder where = new StringBuilder(" WHERE 1 = 1 ");
        Map<String, Object> params = new HashMap<>();
        handleQuery(status, request, where, params);
        query.append(where);
        query.append(" GROUP BY c.id ");

        Query dataQuery = entityManager.createNativeQuery(query.toString(), Tuple.class);
        for (String key : params.keySet()) {
            dataQuery.setParameter(key, params.get(key));
        }
        List<Tuple> tuples = dataQuery.getResultList();
        List<CategoryDistributionResponse> res = tuples.stream().map(
                tuple -> CategoryDistributionResponse.builder()
                        .categoryName(tuple.get("categoryName", String.class))
                        .categoryId(tuple.get("categoryId") != null ? ((Number) tuple.get("categoryId")).longValue() : 0L)
                        .eventCount(tuple.get("eventCount") != null ? ((Number) tuple.get("eventCount")).longValue() : 0L)
                        .build()
        ).toList();
        return res;
    }

    @Override
    public EventApprovalStatResponse getEventApprovalStatResponse(DateRangeFilterRequest request) {
        StringBuilder query = new StringBuilder(
                "SELECT " +
                        " SUM (CASE WHEN e.status = 'APPROVED' THEN 1 ELSE 0 END) AS approvedCount," +
                        " SUM (CASE WHEN e.status = 'REJECTED' THEN 1 ELSE 0 END) AS rejectedCount " +
                        " FROM event e ");

        StringBuilder where = new StringBuilder(" WHERE 1 = 1 ");
        Map<String, Object> params = new HashMap<>();
        handleQuery(null, request, where, params);
        query.append(where);

        Query dataQuery = entityManager.createNativeQuery(query.toString(), Tuple.class);
        for (String key : params.keySet()) {
            dataQuery.setParameter(key, params.get(key));
        }
        List<Tuple> tuples = dataQuery.getResultList();
        List<EventApprovalStatResponse> res = tuples.stream().map(
                tuple -> EventApprovalStatResponse.builder()
                        .approvedCount(tuple.get("approvedCount") != null ? ((Number) tuple.get("approvedCount")).longValue() : 0L)
                        .rejectedCount(tuple.get("rejectedCount") != null ? ((Number) tuple.get("rejectedCount")).longValue() : 0L)
                        .build()
        ).toList();
        return res.getFirst();
    }
}

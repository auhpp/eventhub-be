package com.auhpp.event_management.repository.custom.impl;

import com.auhpp.event_management.dto.request.DateRangeFilterRequest;
import com.auhpp.event_management.dto.request.PaginationFilterRequest;
import com.auhpp.event_management.dto.response.ResaleOverviewResponse;
import com.auhpp.event_management.dto.response.TopResaleEventResponse;
import com.auhpp.event_management.repository.custom.ResaleCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ResaleCustomRepositoryImpl implements ResaleCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private void handleJoin(StringBuilder query) {
        query.append(" INNER JOIN event_session es ON es.event_id = e.id ");
        query.append(" INNER JOIN ticket t ON t.event_session_id = es.id ");
        query.append(" INNER JOIN attendee a ON a.ticket_id = t.id ");
        query.append(" INNER JOIN resale_post rp ON rp.id = a.resale_post_id ");
        query.append(" LEFT JOIN booking b ON b.resale_post_id = rp.id ");
        query.append(" LEFT JOIN attendee buyer_a ON buyer_a.booking_id = b.id ");

    }

    private void handleQuery(DateRangeFilterRequest dateRangeFilterRequest,
                             Long eventId, StringBuilder where, Map<String, Object> params) {

        if (eventId != null) {
            where.append(" AND e.id = :eventId ");
            params.put("eventId", eventId);
        }
        if (dateRangeFilterRequest.getStartDate() != null) {
            where.append(" AND rp.created_at >= :startDate ");
            params.put("startDate", dateRangeFilterRequest.getStartDate());
        }
        if (dateRangeFilterRequest.getEndDate() != null) {
            where.append(" AND rp.created_at <= :endDate");
            params.put("endDate", dateRangeFilterRequest.getEndDate());
        }
    }


    @Override
    public List<TopResaleEventResponse> getTopResaleEvent(
            DateRangeFilterRequest request, PaginationFilterRequest paginationFilterRequest) {
        StringBuilder query = new StringBuilder(
                "SELECT e.id as event_id, " +
                        "       e.name as event_name, " +
                        "       COUNT(DISTINCT rp.id) as resale_post_count, " +
                        "       COUNT(DISTINCT b.id) as completed_transaction_count,  " +
                        "       COALESCE(SUM(buyer_a.price * (rp.commission_rate / 100.0)), 0.0) as total_resale_commission " +
                        "FROM event e  ");
        handleJoin(query);

        StringBuilder where = new StringBuilder(" WHERE 1 = 1 ");
        Map<String, Object> params = new HashMap<>();
        handleQuery(request, null, where, params);
        query.append(where);
        query.append(" GROUP BY e.id, e.name ");
        query.append(" ORDER BY completed_transaction_count DESC ");
        query.append(" LIMIT :limitParam ");
        params.put("limitParam", paginationFilterRequest.getLimit());

        Query dataQuery = entityManager.createNativeQuery(query.toString(), Tuple.class);
        for (String key : params.keySet()) {
            dataQuery.setParameter(key, params.get(key));
        }
        List<Tuple> tuples = dataQuery.getResultList();
        List<TopResaleEventResponse> res = tuples.stream().map(
                tuple -> TopResaleEventResponse.builder()
                        .eventName(tuple.get("event_name", String.class))
                        .eventId(tuple.get("event_id") != null ? ((Number) tuple.get("event_id")).longValue() : 0L)
                        .resalePostCount(tuple.get("resale_post_count") != null ? ((Number) tuple.get("resale_post_count")).longValue() : 0L)
                        .completedTransactionCount(tuple.get("completed_transaction_count") != null ?
                                ((Number) tuple.get("completed_transaction_count")).longValue() : 0L)
                        .totalFee((tuple.get("total_resale_commission") != null ?
                                ((Number) tuple.get("total_resale_commission")).doubleValue() : 0L))
                        .build()
        ).toList();
        return res;
    }

    @Override
    public ResaleOverviewResponse getResaleOverviewResponse(DateRangeFilterRequest request, Long eventId) {
        StringBuilder query = new StringBuilder(
                "SELECT COUNT(DISTINCT rp.id) as total_resale_posts, COUNT(DISTINCT b.id) as successful_transactions  " +
                        " FROM event e ");
        handleJoin(query);

        StringBuilder where = new StringBuilder(" WHERE 1 = 1 ");
        Map<String, Object> params = new HashMap<>();
        handleQuery(request, eventId, where, params);
        query.append(where);

        Query dataQuery = entityManager.createNativeQuery(query.toString(), Tuple.class);
        for (String key : params.keySet()) {
            dataQuery.setParameter(key, params.get(key));
        }
        List<Tuple> tuples = dataQuery.getResultList();
        List<ResaleOverviewResponse> res = tuples.stream().map(
                tuple -> ResaleOverviewResponse.builder()
                        .totalResalePosts(tuple.get("total_resale_posts") != null ? ((Number) tuple.get("total_resale_posts")).longValue() : 0L)
                        .successfulTransactions(tuple.get("successful_transactions") != null ?
                                ((Number) tuple.get("successful_transactions")).longValue() : 0L)
                        .build()
        ).toList();
        return res.getFirst();
    }
}

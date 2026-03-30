package com.auhpp.event_management.repository.custom.impl;

import com.auhpp.event_management.constant.BookingType;
import com.auhpp.event_management.constant.SourceType;
import com.auhpp.event_management.dto.request.DateRangeFilterRequest;
import com.auhpp.event_management.dto.request.PaginationFilterRequest;
import com.auhpp.event_management.dto.response.RevenueChartResponse;
import com.auhpp.event_management.dto.response.TopEventRevenueResponse;
import com.auhpp.event_management.dto.response.TopOrganizerResponse;
import com.auhpp.event_management.repository.custom.AttendeeCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AttendeeCustomRepositoryImpl implements AttendeeCustomRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private void handleJoin(SourceType sourceType, Long eventSessionId, StringBuilder query, boolean joinOrganizer) {
        if (eventSessionId != null) {
            query.append(" INNER JOIN ticket t ON t.id = a.ticket_id ");
        }
        if (sourceType.equals(SourceType.PURCHASE)) {
            if (eventSessionId == null) {
                query.append(" INNER JOIN ticket t ON t.id = a.ticket_id ");
            }
            query.append(" INNER JOIN event_session es ON es.id = t.event_session_id ");
            query.append(" INNER JOIN event e ON e.id = es.event_id ");
        } else if (sourceType.equals(SourceType.RESALE)) {
            query.append(" INNER JOIN booking b ON a.booking_id = b.id ");
            query.append(" INNER JOIN resale_post rp ON rp.id = b.resale_post_id ");
        }
        if (joinOrganizer) {
            query.append(" INNER JOIN app_user u ON u.id = e.app_user_id ");
        }

    }

    private void handleQuery(Long eventSessionId, SourceType sourceType,
                             DateRangeFilterRequest dateRangeFilterRequest, StringBuilder where, Map<String, Object> params) {
        if (eventSessionId != null) {
            where.append(" AND t.event_session_id = :eventSessionId");
            params.put("eventSessionId", eventSessionId);
        }
        if (dateRangeFilterRequest.getStartDate() != null) {
            where.append(" AND a.created_at >= :startDate ");
            params.put("startDate", dateRangeFilterRequest.getStartDate());
        }
        if (dateRangeFilterRequest.getEndDate() != null) {
            where.append(" AND a.created_at <= :endDate ");
            params.put("endDate", dateRangeFilterRequest.getEndDate());
        }
        if (sourceType != null) {
            where.append(" AND a.source_type = :sourceType ");
            params.put("sourceType", sourceType.name());
        }
    }

    @Override
    public List<RevenueChartResponse> getCommissionWithTimeLabel(Long eventSessionId, SourceType sourceType,
                                                                 DateRangeFilterRequest dateRangeFilterRequest) {
        StringBuilder query = new StringBuilder(
                "SELECT  ");
        if (sourceType.equals(SourceType.PURCHASE)) {
            query.append(" COALESCE(SUM(a.price * (e.commission_rate / 100) + " +
                    " e.commission_fixed_per_ticket), 0) as commission,  ");
        } else if (sourceType.equals(SourceType.RESALE)) {
            query.append(" COALESCE(SUM(a.price * (rp.commission_rate / 100)), 0) as commission,  ");
        }
        query.append(" TO_CHAR(a.created_at, 'MM-YYYY') as time_label " +
                "  FROM attendee a ");
        handleJoin(sourceType, eventSessionId, query, false);

        StringBuilder where = new StringBuilder(" WHERE 1 = 1 ");
        Map<String, Object> params = new HashMap<>();
        handleQuery(eventSessionId, sourceType, dateRangeFilterRequest, where, params);

        query.append(where);
        query.append(" GROUP BY TO_CHAR(a.created_at, 'MM-YYYY') ");
        query.append(" ORDER BY TO_CHAR(a.created_at, 'MM-YYYY') ASC");

        Query dataQuery = entityManager.createNativeQuery(query.toString(), Tuple.class);
        for (String key : params.keySet()) {
            dataQuery.setParameter(key, params.get(key));
        }
        List<Tuple> tuples = dataQuery.getResultList();
        List<RevenueChartResponse> res = tuples.stream().map(
                tuple -> RevenueChartResponse.builder()
                        .timeLabel(tuple.get("time_label", String.class))
                        .commission(tuple.get("commission") != null ? ((Number) tuple.get("commission")).doubleValue() : 0.0)
                        .build()
        ).toList();
        return res;
    }

    @Override
    public List<TopEventRevenueResponse> getTopEventRevenue(
            DateRangeFilterRequest request, PaginationFilterRequest paginationFilterRequest) {
        StringBuilder query = new StringBuilder(
                "SELECT COALESCE(SUM(a.price * (e.commission_rate / 100) + " +
                        " e.commission_fixed_per_ticket), 0) as totalCommission, " +
                        " e.id as event_id, " +
                        " e.name as event_name, " +
                        " u.full_name as organizer_name, " +
                        " COALESCE(SUM(t.sold_quantity), 0) as totalTicketsSold " +
                        " FROM attendee a ");
        handleJoin(SourceType.PURCHASE, null, query, true);

        StringBuilder where = new StringBuilder(" WHERE 1 = 1 ");
        Map<String, Object> params = new HashMap<>();
        handleQuery(null, null, request, where, params);
        query.append(where);

        query.append(" GROUP BY e.id, e.name, u.id, u.full_name ");
        query.append(" ORDER BY totalCommission DESC ");
        query.append(" LIMIT :limitParam");
        params.put("limitParam", paginationFilterRequest.getLimit());

        Query dataQuery = entityManager.createNativeQuery(query.toString(), TopEventRevenueResponse.class);
        for (String key : params.keySet()) {
            dataQuery.setParameter(key, params.get(key));
        }
        return dataQuery.getResultList();
    }

    @Override
    public List<TopOrganizerResponse> getTopOrganizerRevenue(
            DateRangeFilterRequest request, PaginationFilterRequest paginationFilterRequest) {
        StringBuilder query = new StringBuilder(
                "SELECT COALESCE(SUM(a.price * (e.commission_rate / 100) + " +
                        " e.commission_fixed_per_ticket), 0) as totalCommissionGenerated, " +
                        " u.id as organizer_id, " +
                        " COUNT(DISTINCT e.id) as event_count, " +
                        " u.full_name as organizer_name " +
                        " FROM attendee a ");
        handleJoin(SourceType.PURCHASE, null, query, true);

        StringBuilder where = new StringBuilder(" WHERE 1 = 1 ");
        Map<String, Object> params = new HashMap<>();
        handleQuery(null, null, request, where, params);
        query.append(where);

        query.append(" GROUP BY u.id, u.full_name ");
        query.append(" ORDER BY totalCommissionGenerated DESC ");
        query.append(" LIMIT :limitParam");
        params.put("limitParam", paginationFilterRequest.getLimit());

        Query dataQuery = entityManager.createNativeQuery(query.toString(), TopOrganizerResponse.class);
        for (String key : params.keySet()) {
            dataQuery.setParameter(key, params.get(key));
        }
        return dataQuery.getResultList();
    }

}

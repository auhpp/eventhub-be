package com.auhpp.event_management.repository.custom.impl;

import com.auhpp.event_management.constant.SourceType;
import com.auhpp.event_management.dto.request.DateRangeFilterRequest;
import com.auhpp.event_management.dto.request.PaginationFilterRequest;
import com.auhpp.event_management.dto.request.StatsFilterRequest;
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
        query.append(" ORDER BY MIN(a.created_at) ASC");

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
    public List<TopEventRevenueResponse> getTopEventRevenue(StatsFilterRequest request) {
        StringBuilder query = new StringBuilder("" +
                "SELECT e.id as event_id, " +
                " e.name as event_name, " +
                " u.full_name as organizer_name, " +

                " COALESCE(SUM(CASE " +
                "       WHEN a.type = 'INVITE' OR t.price = 0 THEN 0.0 " +
                "       ELSE (a.price * (e.commission_rate / 100.0) + e.commission_fixed_per_ticket) " +
                " END), 0.0) as total_commission, " +
                " COALESCE(SUM(a.final_price), 0) as total_revenue, " +

                " COALESCE(MAX(cap.total_sold), 0) as total_tickets_sold, " +
                " COUNT(DISTINCT CASE WHEN a.status = 'CHECKED_IN' THEN a.id END) as total_checkIn, " +

                " COALESCE(AVG(r.rating), 0) as average_rating, " +
                " COUNT(DISTINCT r.id) as review_count," +
                " COALESCE(MAX(cap.total_capacity), 0) as total_tickets " +

                " FROM attendee a ");
        handleJoin(SourceType.PURCHASE, null, query, true);
        query.append(" LEFT JOIN review r ON r.attendee_id = a.id AND r.event_session_id = es.id ");
        query.append(" LEFT JOIN ( " +
                "   SELECT es_sub.event_id, SUM(t_sub.quantity) as total_capacity," +
                "          SUM(t_sub.sold_quantity) as total_sold " +
                "   FROM ticket t_sub " +
                "   JOIN event_session es_sub ON t_sub.event_session_id = es_sub.id " +
                "   GROUP BY es_sub.event_id " +
                " ) cap ON cap.event_id = e.id ");

        StringBuilder where = new StringBuilder(" WHERE 1 = 1 ");
        Map<String, Object> params = new HashMap<>();

        if (request.getOrganizerId() != null) {
            where.append(" AND u.id = :organizerId ");
            params.put("organizerId", request.getOrganizerId());
        }
        if (request.getEventSeriesId() != null) {
            where.append(" AND e.event_series_id = :eventSeriesId ");
            params.put("eventSeriesId", request.getEventSeriesId());
        }
        handleQuery(null, SourceType.PURCHASE, request.getDateRangeFilter(),
                where, params);

        query.append(where);

        query.append(" GROUP BY e.id, e.name, u.id, u.full_name ");
        query.append(" ORDER BY total_revenue DESC ");
        query.append(" LIMIT :limitParam");
        params.put("limitParam", request.getPaginationRequest().getLimit());

        Query dataQuery = entityManager.createNativeQuery(query.toString(), Tuple.class);
        for (String key : params.keySet()) {
            dataQuery.setParameter(key, params.get(key));
        }
        List<Tuple> tuples = dataQuery.getResultList();

        return tuples.stream().map(t -> {
            TopEventRevenueResponse response = new TopEventRevenueResponse();

            response.setEventId(t.get("event_id") != null ? ((Number) t.get("event_id")).longValue() : 0L);
            response.setEventName(t.get("event_name", String.class));
            response.setOrganizerName(t.get("organizer_name", String.class));

            response.setTotalCommission(t.get("total_commission") != null ? ((Number) t.get("total_commission")).doubleValue() : 0.0);
            response.setTotalRevenue(t.get("total_revenue") != null ? ((Number) t.get("total_revenue")).doubleValue() : 0.0);
            response.setTotalTicketsSold(t.get("total_tickets_sold") != null ? ((Number) t.get("total_tickets_sold")).longValue() : 0L);
            response.setTotalCheckIn(t.get("total_checkIn") != null ? ((Number) t.get("total_checkIn")).intValue() : 0);
            response.setTotalTickets(t.get("total_tickets") != null ? ((Number) t.get("total_tickets")).longValue() : 0L);

            // Tròn số thập phân cho đẹp (nếu cần)
            double avgRating = t.get("average_rating") != null ? ((Number) t.get("average_rating")).doubleValue() : 0.0;
            response.setAverageRating(Math.round(avgRating * 10.0) / 10.0); // Ví dụ: 4.56 -> 4.6

            response.setReviewCount(t.get("review_count") != null ? ((Number) t.get("review_count")).intValue() : 0);

            return response;
        }).toList();
    }


    @Override
    public List<TopOrganizerResponse> getTopOrganizerRevenue(
            DateRangeFilterRequest request, PaginationFilterRequest paginationFilterRequest) {
        StringBuilder query = new StringBuilder(
                "SELECT COALESCE(SUM(a.price * (e.commission_rate / 100.0) + " +
                        " e.commission_fixed_per_ticket), 0.0) as total_commission_generated, " +
                        " u.id as organizer_id, " +
                        " COUNT(DISTINCT e.id) as event_count, " +
                        " u.full_name as organizer_name " +
                        " FROM attendee a ");
        handleJoin(SourceType.PURCHASE, null, query, true);

        StringBuilder where = new StringBuilder(" WHERE 1 = 1 ");
        Map<String, Object> params = new HashMap<>();
        handleQuery(null, SourceType.PURCHASE, request, where, params);
        query.append(where);

        query.append(" GROUP BY u.id, u.full_name ");
        query.append(" ORDER BY total_commission_generated DESC ");
        query.append(" LIMIT :limitParam");
        params.put("limitParam", paginationFilterRequest.getLimit());

        Query dataQuery = entityManager.createNativeQuery(query.toString(), Tuple.class);
        for (String key : params.keySet()) {
            dataQuery.setParameter(key, params.get(key));
        }
        List<Tuple> tuples = dataQuery.getResultList();
        return tuples.stream().map(t -> {
            TopOrganizerResponse response = new TopOrganizerResponse();
            response.setOrganizerId(t.get("organizer_id") != null ? ((Number) t.get("organizer_id")).longValue() : 0L);
            response.setOrganizerName(t.get("organizer_name", String.class));
            response.setEventCount(t.get("event_count") != null ? ((Number) t.get("event_count")).longValue() : 0);
            response.setTotalCommissionGenerated(t.get("total_commission_generated") != null ? ((Number) t.get("total_commission_generated")).doubleValue() : 0.0);
            return response;
        }).toList();
    }

}

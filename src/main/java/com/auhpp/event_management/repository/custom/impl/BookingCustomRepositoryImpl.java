package com.auhpp.event_management.repository.custom.impl;

import com.auhpp.event_management.constant.BookingType;
import com.auhpp.event_management.dto.request.StatsFilterRequest;
import com.auhpp.event_management.dto.response.RevenueChartResponse;
import com.auhpp.event_management.repository.custom.BookingCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BookingCustomRepositoryImpl implements BookingCustomRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private void handleQuery(StatsFilterRequest request, BookingType bookingType,
                             StringBuilder where, Map<String, Object> params) {
        if (request.getOrganizerId() != null) {
            where.append(" AND EXISTS (" +
                    " SELECT 1 FROM attendee a " +
                    " INNER JOIN ticket t ON t.id = a.ticket_id " +
                    " INNER JOIN event_session es ON es.id = t.event_session_id " +
                    " INNER JOIN event e ON e.id = es.event_id " +
                    " WHERE a.booking_id = b.id AND e.app_user_id = :organizerId " +
                    ")");
            params.put("organizerId", request.getOrganizerId());
        }
        if (request.getEventSessionId() != null) {
            where.append(" AND EXISTS (" +
                    " SELECT 1 FROM attendee a " +
                    " INNER JOIN ticket t ON t.id = a.ticket_id " +
                    " WHERE a.booking_id = b.id AND t.event_session_id = :eventSessionId " +
                    ")");
            params.put("eventSessionId", request.getEventSessionId());
        }
        if (request.getDateRangeFilter().getStartDate() != null) {
            where.append(" AND b.created_at >= :startDate ");
            params.put("startDate", request.getDateRangeFilter().getStartDate());
        }
        if (request.getDateRangeFilter().getEndDate() != null) {
            where.append(" AND b.created_at <= :endDate");
            params.put("endDate", request.getDateRangeFilter().getEndDate());
        }
        if (bookingType != null) {
            where.append(" AND b.type = :bookingType ");
            params.put("bookingType", bookingType.name());
        }
        if (request.getEventSeriesId() != null) {
            where.append(" AND EXISTS (" +
                    " SELECT 1 FROM attendee a " +
                    " INNER JOIN ticket t ON t.id = a.ticket_id " +
                    " INNER JOIN event_session es ON es.id = t.event_session_id " +
                    " INNER JOIN event e ON e.id = es.event_id " +
                    " WHERE a.booking_id = b.id AND e.event_series_id = :eventSeriesId " +
                    ")");
            params.put("eventSeriesId", request.getEventSeriesId());
        }
    }

    @Override
    public List<RevenueChartResponse> getVoucherRevenueWithTimeLabel(StatsFilterRequest request,
                                                                     BookingType bookingType) {
        StringBuilder query = new StringBuilder(
                "SELECT  TO_CHAR(b.created_at, 'MM-YYYY') as time_label," +
                        "  COALESCE(SUM(b.final_amount), 0) as gmv " +
                        "  FROM booking b ");
        StringBuilder where = new StringBuilder(" WHERE 1 = 1 ");
        Map<String, Object> params = new HashMap<>();
        handleQuery(request, bookingType, where, params);
        query.append(where);
        query.append(" GROUP BY TO_CHAR(b.created_at, 'MM-YYYY') ");
        query.append(" ORDER BY MIN(b.created_at) ASC");
        Query dataQuery = entityManager.createNativeQuery(query.toString(), Tuple.class);
        for (String key : params.keySet()) {
            dataQuery.setParameter(key, params.get(key));
        }
        List<Tuple> tuples = dataQuery.getResultList();
        List<RevenueChartResponse> res = tuples.stream().map(
                tuple -> RevenueChartResponse.builder()
                        .timeLabel(tuple.get("time_label", String.class))
                        .gmv(tuple.get("gmv") != null ? ((Number) tuple.get("gmv")).doubleValue() : 0.0)
                        .build()
        ).toList();
        return res;
    }


}

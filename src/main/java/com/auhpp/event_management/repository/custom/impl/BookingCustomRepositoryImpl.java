package com.auhpp.event_management.repository.custom.impl;

import com.auhpp.event_management.constant.BookingType;
import com.auhpp.event_management.dto.request.DateRangeFilterRequest;
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

    private void handleQuery(Long eventSessionId, BookingType bookingType,
                             DateRangeFilterRequest dateRangeFilterRequest, StringBuilder where, Map<String, Object> params) {
        if (eventSessionId != null) {
            where.append(" AND b.id IN (" +
                    " SELECT DISTINCT b2.id FROM booking b2 " +
                    " INNER JOIN attendee a ON a.booking_id = b2.id " +
                    " INNER JOIN ticket t ON t.id = a.ticket_id " +
                    " WHERE t.event_session_id = :eventSessionId " +
                    ")");
            params.put("eventSessionId", eventSessionId);
        }
        if (dateRangeFilterRequest.getStartDate() != null) {
            where.append(" AND b.created_at >= :startDate ");
            params.put("startDate", dateRangeFilterRequest.getStartDate());
        }
        if (dateRangeFilterRequest.getEndDate() != null) {
            where.append(" AND b.created_at <= :endDate");
            params.put("endDate", dateRangeFilterRequest.getEndDate());
        }
        if (bookingType != null) {
            where.append(" AND b.type = :bookingType ");
            params.put("bookingType", bookingType.name());
        }
    }

    @Override
    public List<RevenueChartResponse> getVoucherRevenueWithTimeLabel(Long eventSessionId, BookingType bookingType,
                                                                     DateRangeFilterRequest dateRangeFilterRequest) {
        StringBuilder query = new StringBuilder(
                "SELECT  TO_CHAR(b.created_at, 'MM-YYYY') as time_label," +
                        "  COALESCE(SUM(b.final_amount), 0) as gmv " +
                        "  FROM booking b ");
        StringBuilder where = new StringBuilder(" WHERE 1 = 1 ");
        Map<String, Object> params = new HashMap<>();
        handleQuery(eventSessionId, bookingType, dateRangeFilterRequest, where, params);
        query.append(where);
        query.append(" GROUP BY TO_CHAR(b.created_at, 'MM-YYYY') ");
        query.append(" ORDER BY TO_CHAR(b.created_at, 'MM-YYYY') ASC");
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

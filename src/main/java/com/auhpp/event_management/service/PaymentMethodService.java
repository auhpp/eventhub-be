package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.PaymentMethodCreateRequest;
import com.auhpp.event_management.dto.request.PaymentMethodUpdateRequest;
import com.auhpp.event_management.dto.response.PaymentMethodResponse;

import java.util.List;

public interface PaymentMethodService {
    PaymentMethodResponse create(PaymentMethodCreateRequest request);

    PaymentMethodResponse update(Long id, PaymentMethodUpdateRequest request);

    void delete(Long id);

    List<PaymentMethodResponse> getAll();
}

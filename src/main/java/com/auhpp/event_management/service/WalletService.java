package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.WalletCreateRequest;
import com.auhpp.event_management.dto.request.WalletSearchRequest;
import com.auhpp.event_management.dto.request.WalletUpdateRequest;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.WalletResponse;
import com.auhpp.event_management.entity.Booking;

public interface WalletService {
    WalletResponse create(WalletCreateRequest request);

    WalletResponse update(Long id, WalletUpdateRequest request);

    PageResponse<WalletResponse> filter(WalletSearchRequest request, int page, int size);

    WalletResponse findById(Long id);

    void processSingleBooking(Booking booking);

}

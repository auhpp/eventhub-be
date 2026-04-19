package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.request.WalletTransactionCreateRequest;
import com.auhpp.event_management.dto.request.WalletTransactionSearchRequest;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.WalletTransactionResponse;

public interface WalletTransactionService {
    WalletTransactionResponse create(WalletTransactionCreateRequest request);

    PageResponse<WalletTransactionResponse> filter(WalletTransactionSearchRequest request, int page, int size);


}

package com.auhpp.event_management.service;

import com.alibaba.excel.ExcelWriter;
import com.auhpp.event_management.dto.request.WithdrawalRequestCreateRequest;
import com.auhpp.event_management.dto.request.WithdrawalRequestSearchRequest;
import com.auhpp.event_management.dto.request.WithdrawalRequestUpdateRequest;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.WithdrawalRequestResponse;

public interface WithdrawalRequestService {
    WithdrawalRequestResponse create(WithdrawalRequestCreateRequest request);

    WithdrawalRequestResponse update(Long id, WithdrawalRequestUpdateRequest request);

    PageResponse<WithdrawalRequestResponse> filter(WithdrawalRequestSearchRequest request, int page, int size);

    WithdrawalRequestResponse findById(Long id);

    void exportReportWithdrawal(ExcelWriter excelWriter, WithdrawalRequestSearchRequest request);

}

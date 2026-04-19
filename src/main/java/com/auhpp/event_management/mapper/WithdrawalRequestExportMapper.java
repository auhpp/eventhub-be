package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.response.WithdrawalRequestExcelReportResponse;
import com.auhpp.event_management.entity.WithdrawalRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WithdrawalRequestExportMapper {

    @Mapping(target = "userEmail", source = "wallet.appUser.email")
    @Mapping(target = "userName", source = "wallet.appUser.fullName")
    WithdrawalRequestExcelReportResponse toWithdrawalRequestExcelReportResponse(WithdrawalRequest withdrawalRequest);
}

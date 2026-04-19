package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.request.WithdrawalRequestCreateRequest;
import com.auhpp.event_management.dto.request.WithdrawalRequestUpdateRequest;
import com.auhpp.event_management.dto.response.WithdrawalRequestResponse;
import com.auhpp.event_management.entity.WithdrawalRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UserBasicMapper.class})
public interface WithdrawalRequestMapper {
    WithdrawalRequestResponse toWithdrawalRequestResponse(WithdrawalRequest withdrawalRequest);

    @Mapping(target = "proofImageUrl", ignore = true)
    WithdrawalRequest toWithdrawalRequest(WithdrawalRequestCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(WithdrawalRequestUpdateRequest request, @MappingTarget WithdrawalRequest withdrawalRequest);
}

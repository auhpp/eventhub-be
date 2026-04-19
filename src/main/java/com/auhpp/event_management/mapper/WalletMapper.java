package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.request.WalletUpdateRequest;
import com.auhpp.event_management.dto.response.WalletResponse;
import com.auhpp.event_management.entity.Wallet;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {UserBasicMapper.class})
public interface WalletMapper {
    WalletResponse toWalletResponse(Wallet wallet);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(WalletUpdateRequest request, @MappingTarget Wallet wallet);
}

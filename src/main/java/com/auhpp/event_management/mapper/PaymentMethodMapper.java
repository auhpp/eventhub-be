package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.request.PaymentMethodCreateRequest;
import com.auhpp.event_management.dto.request.PaymentMethodUpdateRequest;
import com.auhpp.event_management.dto.response.PaymentMethodResponse;
import com.auhpp.event_management.entity.PaymentMethod;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface PaymentMethodMapper {
    PaymentMethod toPaymentMethod(PaymentMethodCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(PaymentMethodUpdateRequest request, @MappingTarget PaymentMethod paymentMethod);

    PaymentMethodResponse toPaymentMethodResponse(PaymentMethod paymentMethod);
}

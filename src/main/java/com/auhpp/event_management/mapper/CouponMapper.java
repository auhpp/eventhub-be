package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.request.CouponCreateRequest;
import com.auhpp.event_management.dto.request.CouponUpdateRequest;
import com.auhpp.event_management.dto.response.CouponResponse;
import com.auhpp.event_management.entity.Coupon;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CouponMapper {
    @Mapping(target = "quantityUsage", expression = "java(coupon.getBookings() != null ? coupon.getBookings().size() : 0)")
    CouponResponse toCouponResponse(Coupon coupon);

    Coupon toCoupon(CouponCreateRequest request);

    @Mapping(target = "avatarUrl", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCoupon(CouponUpdateRequest request, @MappingTarget Coupon coupon);


}

package com.auhpp.event_management.repository.custom;

import com.auhpp.event_management.constant.RoleName;
import com.auhpp.event_management.dto.request.DateRangeFilterRequest;
import com.auhpp.event_management.dto.response.UserGrowthResponse;

import java.util.List;

public interface AppUserCustomRepository {
    List<UserGrowthResponse> getUserGrowthResponse(DateRangeFilterRequest request, RoleName roleName);
}

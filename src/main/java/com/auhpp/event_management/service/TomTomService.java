package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.response.MapAddressResponse;

import java.util.List;

public interface TomTomService {
    List<MapAddressResponse> searchAddresses(String query);
}

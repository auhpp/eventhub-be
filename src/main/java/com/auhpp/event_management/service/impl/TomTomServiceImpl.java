package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.dto.response.MapAddressResponse;
import com.auhpp.event_management.dto.response.PointResponse;
import com.auhpp.event_management.service.TomTomService;
import com.auhpp.event_management.util.AddressUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class TomTomServiceImpl implements TomTomService {
    private final RestClient restClient;

    @Value("${app.tomtom.api-key}")
    private String apiKey;

    public TomTomServiceImpl(@Qualifier("tomTomRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public List<MapAddressResponse> searchAddresses(String query) {
        JsonNode response = restClient.get().uri(
                uriBuilder -> uriBuilder.path("/search/2/geocode/{query}.json")
                        .queryParam("key", apiKey)
                        .queryParam("language", "vi-VN")
                        .queryParam("countrySet", "VN/VNM")
                        .queryParam("limit", 100)
                        .build(query)
        ).retrieve().body(JsonNode.class);
        if (response != null) {
            JsonNode results = response.path("results");
            if (results.isArray()) {
                return StreamSupport.stream(results.spliterator(), false)
                        .map(
                                it -> MapAddressResponse.builder()
                                        .name(
                                                AddressUtils.formatTomTomAddress(
                                                        it.path("address").path("freeformAddress").asText()
                                                )
                                        )
                                        .point(PointResponse.builder()
                                                .latitude(it.path("position").path("lat").asDouble())
                                                .longitude(it.path("position").path("lon").asDouble())
                                                .build())
                                        .build()
                        ).toList();
            }
        }
        return List.of();
    }
}

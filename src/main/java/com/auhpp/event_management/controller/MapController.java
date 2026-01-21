package com.auhpp.event_management.controller;

import com.auhpp.event_management.dto.response.MapAddressResponse;
import com.auhpp.event_management.service.TomTomService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/map")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MapController {

    TomTomService tomTomService;

    @GetMapping("/geocode")
    public ResponseEntity<List<MapAddressResponse>> searchAddresses(
            @RequestParam(name = "query") String query) {
        List<MapAddressResponse> responses = tomTomService.searchAddresses(query);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responses);
    }
}

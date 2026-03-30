package com.auhpp.event_management.controller;

import com.auhpp.event_management.service.FavoriteService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/favorite")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FavoriteController {
    FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<Void> create(
            @RequestParam("eventId") Long eventId
    ) {
        favoriteService.create(eventId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(
            @RequestParam("eventId") Long eventId
    ) {
        favoriteService.delete(eventId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping
    public ResponseEntity<List<Long>> getEventIdFavorite(
    ) {
        List<Long> res = favoriteService.getEventIdFavorite();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }


}

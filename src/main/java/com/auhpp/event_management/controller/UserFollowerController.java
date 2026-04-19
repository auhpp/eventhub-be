package com.auhpp.event_management.controller;

import com.auhpp.event_management.dto.request.UserFollowerCreateRequest;
import com.auhpp.event_management.dto.request.UserFollowerSearchRequest;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.UserFollowerResponse;
import com.auhpp.event_management.service.UserFollowerService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user-follower")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserFollowerController {
    UserFollowerService userFollowerService;

    @PostMapping
    public ResponseEntity<UserFollowerResponse> createUserFollower(
            @Valid @RequestBody UserFollowerCreateRequest request
    ) {
        UserFollowerResponse result = userFollowerService.createUserFollower(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserFollower(
            @PathVariable("id") Long id
    ) {
        userFollowerService.deleteUserFollower(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/filter")
    public ResponseEntity<PageResponse<UserFollowerResponse>> getUserFollowers(
            @RequestBody  UserFollowerSearchRequest request,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<UserFollowerResponse> result = userFollowerService.getUserFollowers(
                request, page, size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> countUserFollowers(
            UserFollowerSearchRequest request
    ) {
        Integer result = userFollowerService.countUserFollowers(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }
}

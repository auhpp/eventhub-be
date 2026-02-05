package com.auhpp.event_management.controller;

import com.auhpp.event_management.dto.request.PasswordChangeRequest;
import com.auhpp.event_management.dto.request.SocialLinkCreateRequest;
import com.auhpp.event_management.dto.request.UserUpdateRequest;
import com.auhpp.event_management.dto.response.SocialLinkResponse;
import com.auhpp.event_management.dto.response.UserBasicResponse;
import com.auhpp.event_management.service.AppUserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class UserController {
    AppUserService userService;

    @PutMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserBasicResponse> updateInfoUser(
            @PathVariable("userId") Long id,
            @Valid @ModelAttribute UserUpdateRequest request
    ) {
        UserBasicResponse result = userService.updateInfoUser(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PutMapping(value = "/change-password/{userId}")
    public ResponseEntity<UserBasicResponse> changePassword(
            @PathVariable("userId") Long id,
            @Valid @RequestBody PasswordChangeRequest request
    ) {
        userService.changePassword(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping(value = "/social-link")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<List<SocialLinkResponse>> createSocialLink(
            List<@Valid SocialLinkCreateRequest> requests
    ) {
        List<SocialLinkResponse> responses = userService.createSocialLink(requests);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responses);
    }

    @GetMapping
    public ResponseEntity<UserBasicResponse> getByEmail(
            @RequestParam("email") String email
    ) {
        UserBasicResponse result = userService.getByEmail(email);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }
}

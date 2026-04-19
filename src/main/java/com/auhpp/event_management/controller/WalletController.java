package com.auhpp.event_management.controller;

import com.auhpp.event_management.constant.WalletStatus;
import com.auhpp.event_management.dto.request.WalletSearchRequest;
import com.auhpp.event_management.dto.request.WalletUpdateRequest;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.WalletResponse;
import com.auhpp.event_management.service.WalletService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WalletController {
    WalletService walletService;

    @PutMapping("/{id}")
    public ResponseEntity<WalletResponse> update(
            @PathVariable("id") Long id,
            @RequestBody WalletStatus status
    ) {
        WalletResponse result = walletService.update(id, WalletUpdateRequest.builder()
                .status(status)
                .build());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping("/filter")
    public ResponseEntity<PageResponse<WalletResponse>> filter(
            @RequestBody WalletSearchRequest request,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<WalletResponse> result = walletService.filter(request, page, size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<WalletResponse> findById(
            @PathVariable("id") Long id
    ) {
        WalletResponse result = walletService.findById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }
}

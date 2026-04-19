package com.auhpp.event_management.controller;

import com.auhpp.event_management.dto.request.WalletTransactionSearchRequest;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.WalletTransactionResponse;
import com.auhpp.event_management.service.WalletTransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wallet-transaction")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WalletTransactionController {
    WalletTransactionService walletTransactionService;

    @PostMapping("/filter")
    public ResponseEntity<PageResponse<WalletTransactionResponse>> filter(
            @RequestBody WalletTransactionSearchRequest request,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<WalletTransactionResponse> result = walletTransactionService.filter(request, page, size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

}

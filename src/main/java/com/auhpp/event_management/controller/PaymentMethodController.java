package com.auhpp.event_management.controller;

import com.auhpp.event_management.dto.request.PaymentMethodCreateRequest;
import com.auhpp.event_management.dto.request.PaymentMethodUpdateRequest;
import com.auhpp.event_management.dto.response.PaymentMethodResponse;
import com.auhpp.event_management.service.PaymentMethodService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment-method")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentMethodController {
    PaymentMethodService paymentMethodService;

    @PostMapping
    public ResponseEntity<PaymentMethodResponse> create(
            @Valid @RequestBody PaymentMethodCreateRequest request
    ) {
        PaymentMethodResponse result = paymentMethodService.create(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentMethodResponse> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody PaymentMethodUpdateRequest request
    ) {
        PaymentMethodResponse result = paymentMethodService.update(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id") Long id) {
        paymentMethodService.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping
    public ResponseEntity<List<PaymentMethodResponse>> getAll() {
        List<PaymentMethodResponse> res = paymentMethodService.getAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }
}

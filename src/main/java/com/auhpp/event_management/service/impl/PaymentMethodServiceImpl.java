package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.dto.request.PaymentMethodCreateRequest;
import com.auhpp.event_management.dto.request.PaymentMethodUpdateRequest;
import com.auhpp.event_management.dto.response.PaymentMethodResponse;
import com.auhpp.event_management.entity.AppUser;
import com.auhpp.event_management.entity.PaymentMethod;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.PaymentMethodMapper;
import com.auhpp.event_management.repository.AppUserRepository;
import com.auhpp.event_management.repository.PaymentMethodRepository;
import com.auhpp.event_management.service.PaymentMethodService;
import com.auhpp.event_management.util.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PaymentMethodServiceImpl implements PaymentMethodService {
    AppUserRepository appUserRepository;
    PaymentMethodRepository paymentMethodRepository;
    PaymentMethodMapper paymentMethodMapper;

    @Override
    @Transactional
    public PaymentMethodResponse create(PaymentMethodCreateRequest request) {
        AppUser currentUser = appUserRepository.findByEmail(SecurityUtils.getCurrentUserLogin()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        PaymentMethod paymentMethod = null;
        if (currentUser.getPaymentMethods() != null) {
            if (currentUser.getPaymentMethods().size() < 20) {
                paymentMethod = paymentMethodMapper.toPaymentMethod(request);
                paymentMethod.setAppUser(currentUser);
                paymentMethodRepository.save(paymentMethod);
            } else {
                throw new AppException(ErrorCode.INVALID_PARAMS);
            }
        }
        return paymentMethodMapper.toPaymentMethodResponse(paymentMethod);
    }

    @Override
    @Transactional
    public PaymentMethodResponse update(Long id, PaymentMethodUpdateRequest request) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        paymentMethodMapper.update(request, paymentMethod);
        paymentMethodRepository.save(paymentMethod);
        return paymentMethodMapper.toPaymentMethodResponse(paymentMethod);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        paymentMethodRepository.deleteById(id);
    }

    @Override
    public List<PaymentMethodResponse> getAll() {
        AppUser currentUser = appUserRepository.findByEmail(SecurityUtils.getCurrentUserLogin()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        return currentUser.getPaymentMethods() != null ?
                currentUser.getPaymentMethods().stream().map(paymentMethodMapper::toPaymentMethodResponse).toList()
                : List.of();
    }
}

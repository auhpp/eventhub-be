package com.auhpp.event_management.validation.validator;

import com.auhpp.event_management.dto.request.TicketUpdateRequest;
import com.auhpp.event_management.validation.annotation.ValidUpdateTicket;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TicketUpdateValidator implements ConstraintValidator<ValidUpdateTicket, TicketUpdateRequest> {
    @Override
    public void initialize(ValidUpdateTicket constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(TicketUpdateRequest request, ConstraintValidatorContext constraintValidatorContext) {
        if (request == null) return true;
        boolean isValid = true;
        if (request.getOpenAt() != null && request.getOpenAt().isAfter(request.getEndAt())) {
            customMessage(constraintValidatorContext,
                    "openAt", "Ticket open date is invalid");
            isValid = false;
        }

        if (request.getMaximumPerPurchase() != null &&
                request.getQuantity() != null && request.getMaximumPerPurchase() > request.getQuantity()) {
            customMessage(constraintValidatorContext,
                    "maximumPerPurchase", "Ticket maximum per purchase is invalid");
            isValid = false;
        }
        return isValid;
    }

    private void customMessage(ConstraintValidatorContext context, String fieldName, String message) {
        context.disableDefaultConstraintViolation();

        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(fieldName)
                .addConstraintViolation();
    }
}

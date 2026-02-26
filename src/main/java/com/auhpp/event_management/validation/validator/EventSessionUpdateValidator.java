package com.auhpp.event_management.validation.validator;

import com.auhpp.event_management.dto.request.EventSessionUpdateRequest;
import com.auhpp.event_management.validation.annotation.ValidUpdateEventSession;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class EventSessionUpdateValidator implements ConstraintValidator<ValidUpdateEventSession,
        EventSessionUpdateRequest> {
    @Override
    public void initialize(ValidUpdateEventSession constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(EventSessionUpdateRequest request, ConstraintValidatorContext constraintValidatorContext) {
        if (request == null) return true;
        boolean isValid = true;
        if (request.getStartDateTime().isBefore(LocalDateTime.now()) ||
                request.getStartDateTime().isAfter(request.getEndDateTime())) {
            customMessage(constraintValidatorContext,
                    "startDateTime", "Event session startDateTime is invalid");
            isValid = false;
        }
        if (request.getCheckinStartTime().isAfter(request.getStartDateTime())) {
            customMessage(constraintValidatorContext,
                    "checkinStartTime", "Event session check-in start time is invalid");
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

package com.auhpp.event_management.validation.validator;

import com.auhpp.event_management.dto.request.EventUpdateRequest;
import com.auhpp.event_management.validation.annotation.ValidUpdateEvent;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

public class EventUpdateValidator implements ConstraintValidator<ValidUpdateEvent, EventUpdateRequest> {
    @Override
    public void initialize(ValidUpdateEvent constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(EventUpdateRequest request, ConstraintValidatorContext constraintValidatorContext) {
        if (request == null) return true;
        boolean isValid = true;
        if (!StringUtils.hasText(request.getLocation()) || request.getLocationLongitude() != null
                || request.getLocationLatitude() != null) {
            if (!StringUtils.hasText(request.getLocation())) {
                customMessage(constraintValidatorContext,
                        "location", "Event location cannot be empty");
                isValid = false;
            }

            if (request.getLocationLongitude() == null) {
                customMessage(constraintValidatorContext,
                        "locationLongitude", "Event location longitude cannot be null");
                isValid = false;
            }
            if (request.getLocationLatitude() == null) {
                customMessage(constraintValidatorContext,
                        "locationLatitude", "Event location latitude cannot be null");
                isValid = false;
            }
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

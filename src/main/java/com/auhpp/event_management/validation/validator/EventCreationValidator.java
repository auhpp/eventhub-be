package com.auhpp.event_management.validation.validator;

import com.auhpp.event_management.constant.EventType;
import com.auhpp.event_management.dto.request.EventCreateRequest;
import com.auhpp.event_management.validation.annotation.ValidCreateEvent;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

public class EventCreationValidator implements ConstraintValidator<ValidCreateEvent, EventCreateRequest> {
    @Override
    public void initialize(ValidCreateEvent constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(EventCreateRequest eventCreateRequest, ConstraintValidatorContext constraintValidatorContext) {
        if (eventCreateRequest == null) return true;
        boolean isValid = true;
        if (eventCreateRequest.getType() == EventType.OFFLINE) {
            if (!StringUtils.hasText(eventCreateRequest.getLocation())) {
                customMessage(constraintValidatorContext,
                        "location", "Event location cannot be empty");
                isValid = false;
            }
            if (eventCreateRequest.getLocationLongitude() == null) {
                customMessage(constraintValidatorContext,
                        "locationLongitude", "Event location longitude cannot be null");
                isValid = false;
            }
            if (eventCreateRequest.getLocationLatitude() == null) {
                customMessage(constraintValidatorContext,
                        "locationLatitude", "Event location latitude cannot be null");
                isValid = false;
            }
        } else {
            if (!StringUtils.hasText(eventCreateRequest.getMeetingUrl())) {
                customMessage(constraintValidatorContext,
                        "meetingUrl", "Event meeting url cannot be empty");
                isValid = false;
            }
            if (eventCreateRequest.getMeetingPlatform() == null) {
                customMessage(constraintValidatorContext,
                        "meetingPlatform", "Event meeting platform cannot be null");
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

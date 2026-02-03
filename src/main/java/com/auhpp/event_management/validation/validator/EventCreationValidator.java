package com.auhpp.event_management.validation.validator;

import com.auhpp.event_management.constant.EventType;
import com.auhpp.event_management.dto.request.EventCreateRequest;
import com.auhpp.event_management.dto.request.EventSessionCreateRequest;
import com.auhpp.event_management.dto.request.TicketCreateRequest;
import com.auhpp.event_management.validation.annotation.ValidCreateEvent;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

public class EventCreationValidator implements ConstraintValidator<ValidCreateEvent, EventCreateRequest> {
    @Override
    public void initialize(ValidCreateEvent constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(EventCreateRequest eventCreateRequest,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (eventCreateRequest == null) return true;
        boolean isValid = true;
        if (eventCreateRequest.getType() == EventType.OFFLINE) {
            if (!StringUtils.hasText(eventCreateRequest.getLocation())) {
                customMessage(constraintValidatorContext,
                        "location", "Event location cannot be empty");
                isValid = false;
            }
            if (!StringUtils.hasText(eventCreateRequest.getAddress())) {
                customMessage(constraintValidatorContext,
                        "address", "Event address cannot be empty");
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
            for (EventSessionCreateRequest evSessionRe : eventCreateRequest.getEventSessionCreateRequests()) {
                if (!StringUtils.hasText(evSessionRe.getMeetingUrl())) {
                    customMessage(constraintValidatorContext,
                            "meetingUrl", "Event meeting url cannot be empty");
                    isValid = false;
                }
                if (evSessionRe.getMeetingPlatform() == null) {
                    customMessage(constraintValidatorContext,
                            "meetingPlatform", "Event meeting platform cannot be null");
                    isValid = false;
                }
                if (evSessionRe.getStartDateTime().isBefore(LocalDateTime.now()) ||
                        evSessionRe.getStartDateTime().isAfter(evSessionRe.getEndDateTime())) {
                    customMessage(constraintValidatorContext,
                            "startDateTime", "Event session startDateTime is invalid");
                    isValid = false;
                }
                if (evSessionRe.getCheckinStartTime().isAfter(evSessionRe.getStartDateTime())) {
                    customMessage(constraintValidatorContext,
                            "checkinStartTime", "Event session check-in start time is invalid");
                    isValid = false;
                }
                for (TicketCreateRequest ticketCreateRequest : evSessionRe.getTicketCreateRequests()) {
                    if (ticketCreateRequest.getOpenAt().isBefore(LocalDateTime.now()) ||
                            ticketCreateRequest.getOpenAt().isAfter(ticketCreateRequest.getEndAt()) ||
                            ticketCreateRequest.getOpenAt().isAfter(evSessionRe.getStartDateTime())
                    ) {
                        customMessage(constraintValidatorContext,
                                "openAt", "Ticket open date is invalid");
                        isValid = false;
                    }
                    if (ticketCreateRequest.getEndAt().isAfter(evSessionRe.getStartDateTime())) {
                        customMessage(constraintValidatorContext,
                                "endAt", "Ticket end date is invalid");
                        isValid = false;
                    }
                    if (ticketCreateRequest.getMaximumPerPurchase() > ticketCreateRequest.getQuantity()) {
                        customMessage(constraintValidatorContext,
                                "maximumPerPurchase", "Ticket maximum per purchase is invalid");
                        isValid = false;
                    }
                }
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

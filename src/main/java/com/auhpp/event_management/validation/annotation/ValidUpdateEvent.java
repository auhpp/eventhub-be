package com.auhpp.event_management.validation.annotation;

import com.auhpp.event_management.validation.validator.EventUpdateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EventUpdateValidator.class)
public @interface ValidUpdateEvent {
    String message() default "Invalid event information";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

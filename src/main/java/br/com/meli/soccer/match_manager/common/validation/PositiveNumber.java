package br.com.meli.soccer.match_manager.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

import static br.com.meli.soccer.match_manager.common.constants.ValidationFailedMessageConstants.NEGATIVE_NUMBER;

@Documented
@Constraint(validatedBy = PositiveNumberValidator.class)
@Target( { ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PositiveNumber {

    String message() default NEGATIVE_NUMBER;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload()  default {};
}

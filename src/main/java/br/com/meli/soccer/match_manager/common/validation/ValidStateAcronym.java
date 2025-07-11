package br.com.meli.soccer.match_manager.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

import static br.com.meli.soccer.match_manager.common.constants.ValidationFailedMessageConstants.INVALID_STATE_ACRONYM;

@Documented
@Constraint(validatedBy = StateAcronymValidator.class)
@Target( { ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@NotEmpty
@Size(min = 2, max = 2)
public @interface ValidStateAcronym {
    String message() default INVALID_STATE_ACRONYM;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload()  default {};
}

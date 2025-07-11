package br.com.meli.soccer.match_manager.club.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {})
@Target( { ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@NotEmpty
@Size(min = 2, max = 50)
public @interface ValidClubName {

    String message() default "The club name is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload()  default {};
}

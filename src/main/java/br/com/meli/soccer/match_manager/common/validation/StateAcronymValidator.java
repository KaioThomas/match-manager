package br.com.meli.soccer.match_manager.common.validation;

import br.com.meli.soccer.match_manager.common.enums.AcronymStatesEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StateAcronymValidator implements ConstraintValidator<ValidStateAcronym, String> {

    @Override
    public boolean isValid(String acronymState, ConstraintValidatorContext context) {
        return AcronymStatesEnum.isValidAcronym(acronymState);
    }
}

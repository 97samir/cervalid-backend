package com.cervalid.platform.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RucValidator implements ConstraintValidator<RucValid, String> {

    @Override
    public boolean isValid(String ruc, ConstraintValidatorContext context) {

        if (ruc == null || ruc.isBlank()) return false;

        // Debe tener 11 dígitos numéricos
        return ruc.matches("\\d{11}");
    }
}

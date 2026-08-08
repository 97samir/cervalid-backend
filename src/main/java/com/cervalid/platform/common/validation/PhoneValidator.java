package com.cervalid.platform.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator
        implements ConstraintValidator<PhoneValid, String> {

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {

        if (phone == null || phone.isBlank()) return false;

        // Debe tener 11 dígitos numéricos
        return phone.matches("\\d{9}");
    }
}

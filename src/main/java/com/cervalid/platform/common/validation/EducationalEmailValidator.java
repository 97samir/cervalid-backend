package com.cervalid.platform.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EducationalEmailValidator
        implements ConstraintValidator<EducationalEmail, String> {

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {

        // Si es null o vacío → válido (porque es opcional)
        if (email == null || email.isBlank()) {
            return true;
        }

        // Normalizar a minúsculas
        String normalizedEmail = email.toLowerCase().trim();

        // Validar dominios educativos reales
        return normalizedEmail.matches(
                "^[A-Za-z0-9._%+-]+@.+\\.(edu|edu\\.pe)$"
        );
    }
}

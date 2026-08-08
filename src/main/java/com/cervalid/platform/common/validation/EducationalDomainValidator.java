package com.cervalid.platform.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EducationalDomainValidator
        implements ConstraintValidator<EducationalDomain, String> {

    @Override
    public boolean isValid(String domain, ConstraintValidatorContext context) {

        // Si es null o vacío → válido (campo opcional)
        if (domain == null || domain.isBlank()) {
            return true;
        }

        // Normalizar a minúsculas
        String normalizedDomain = domain.toLowerCase().trim();

        // Validar que sea solo dominio educativo (sin http ni rutas)
        return normalizedDomain.matches(
                "^[a-zA-Z0-9.-]+\\.(edu|edu\\.pe)$"
        );
    }
}

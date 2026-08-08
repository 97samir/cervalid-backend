package com.cervalid.platform.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)

public @interface PhoneValid {
    String message() default "El N DE CELULAR debe tener 9 dígitos numéricos válidos";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}






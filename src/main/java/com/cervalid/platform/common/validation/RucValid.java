package com.cervalid.platform.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RucValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)

public @interface RucValid {
    String message() default "El RUC debe tener 11 dígitos numéricos válidos";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

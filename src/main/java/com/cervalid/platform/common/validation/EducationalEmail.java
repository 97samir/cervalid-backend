package com.cervalid.platform.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EducationalEmailValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)

public @interface EducationalEmail {

    String message() default "El correo debe pertenecer a un dominio educativo válido (.edu, .edu.pe)";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

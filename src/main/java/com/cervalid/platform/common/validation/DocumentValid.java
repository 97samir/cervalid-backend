package com.cervalid.platform.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DocumentValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DocumentValid {

    String message() default "Documento inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

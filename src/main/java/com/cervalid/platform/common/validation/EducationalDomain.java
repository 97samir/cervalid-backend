package com.cervalid.platform.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EducationalDomainValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EducationalDomain {

    String message() default "El dominio institucional debe ser educativo (.edu o .edu.pe)";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

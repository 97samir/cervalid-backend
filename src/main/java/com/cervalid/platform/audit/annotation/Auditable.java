package com.cervalid.platform.audit.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented

public @interface Auditable {
    String action();
    String resource();
    String resourceIdField() default "id";

    // permite hacer en clases: @Auditable(action="CREATE", resource="USER")
}

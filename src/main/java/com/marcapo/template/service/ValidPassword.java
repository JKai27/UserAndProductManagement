package com.marcapo.template.service;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "Password must be at least 6 characters long and contain at least one uppercase letter," +
            " one lowercase letter, one number, and one special character (@!%*?&)."
            + "Spaces are not allowed. The following characters are forbidden: % $ § ° ^ ; ` \" > < | # € ~. "
            + " The password must not contain your username.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

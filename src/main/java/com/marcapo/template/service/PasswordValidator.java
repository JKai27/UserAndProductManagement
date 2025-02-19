package com.marcapo.template.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
    private static final Logger logger = LoggerFactory.getLogger(PasswordValidator.class);

    private static final String PASSWORD_PATTERN =
            "^(?!.*[\\s%$§°^;`\"#€~])(?!.*\\bUSERNAME\\b)(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@!%*?&])[A-Za-z\\d@!%*?&]{6,}$";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        logger.info("PasswordValidator is being executed for password: {}", password);

        if (password == null || password.trim().isEmpty()) {
            logger.error("Password validation failed: Password is null or empty");

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Password cannot be empty.")
                    .addConstraintViolation();

            return false;
        }

        boolean matchesPattern = password.matches(PASSWORD_PATTERN);
        logger.info("Password: {} | Matches pattern: {}", password, matchesPattern);

        return matchesPattern;
    }
}
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

        // ✅ Allow null values (only validate if newPassword is provided)
        if (password == null || password.isEmpty()) {
            logger.warn("Skipping password validation because it is null or empty");
            return true;  // ✅ Allow null passwords (for PATCH requests)
        }

        boolean matchesPattern = password.matches(PASSWORD_PATTERN);
        logger.info("Password: {} | Matches pattern: {}", password, matchesPattern);

        return matchesPattern;
    }
}

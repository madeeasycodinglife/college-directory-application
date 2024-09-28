package com.madeeasy.util;

import com.madeeasy.entity.Role;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ValidationUtils {

    public static Map<String, String> validateNotBlank(String value, String fieldName) {
        Map<String, String> errors = new HashMap<>();
        if (value == null || value.isBlank()) {
            errors.put(fieldName, fieldName + " must not be blank");
        }
        return errors;
    }

    public static Map<String, String> validateEmail(String email) {
        Map<String, String> errors = new HashMap<>();
        Map<String, String> notBlankErrors = validateNotBlank(email, "Email");
        if (!notBlankErrors.isEmpty()) {
            return notBlankErrors;
        }
        // Check Email format
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (email != null && !Pattern.matches(emailRegex, email)) {
            errors.put("email", "Email ID should be valid");
        }
        return errors;
    }

    public static Map<String, String> validateRole(String role) {
        Map<String, String> errors = new HashMap<>();
        if (role == null || role.isBlank()) {
            errors.put("Role", "Role must not be blank");
        } else {
            try {
                Role.valueOf(role.toUpperCase());
            } catch (IllegalArgumentException e) {
                errors.put("Role", "Invalid role: " + role);
            }
        }
        return errors;
    }
}

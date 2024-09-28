package com.madeeasy.util;

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

    public static Map<String, String> validatePositiveInteger(Integer value, String fieldName) {
        Map<String, String> errors = new HashMap<>();
        if (value == null || value <= 0) {
            errors.put(fieldName, fieldName + " must be a positive integer");
        }
        return errors;
    }
    public static Map<String, String> validateYear(String year, String fieldName) {
        Map<String, String> errors = new HashMap<>();
        if (year == null || year.isBlank()) {
            errors.put(fieldName, fieldName + " must not be blank");
            return errors;
        }

        // Check if the year is a 4-digit number or greater
        String yearRegex = "^(\\d{4,})$";
        if (!Pattern.matches(yearRegex, year)) {
            errors.put(fieldName, fieldName + " must be a 4-digit number or greater");
        }
        return errors;
    }
}

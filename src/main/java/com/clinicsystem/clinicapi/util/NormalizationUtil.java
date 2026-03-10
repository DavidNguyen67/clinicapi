package com.clinicsystem.clinicapi.util;

import com.clinicsystem.clinicapi.validation.Normalize;

public class NormalizationUtil {

    private NormalizationUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String normalize(String value, Normalize.NormalizeType... types) {
        if (value == null) {
            return null;
        }

        String result = value;

        for (Normalize.NormalizeType type : types) {
            result = applyNormalization(result, type);
        }

        return result;
    }

    private static String applyNormalization(String value, Normalize.NormalizeType type) {
        if (value == null || value.isEmpty()) {
            return value;
        }

        return switch (type) {
            case TRIM -> value.trim();
            case LOWERCASE -> value.toLowerCase();
            case UPPERCASE -> value.toUpperCase();
            case COLLAPSE_WHITESPACE -> value.trim().replaceAll("\\s+", " ");
            case REMOVE_ALL_WHITESPACE -> value.replaceAll("\\s+", "");
            case REMOVE_SPECIAL_CHARS -> value.replaceAll("[^a-zA-Z0-9\\s]", "");
        };
    }

    public static String normalizeEmail(String email) {
        return normalize(email, Normalize.NormalizeType.TRIM, Normalize.NormalizeType.LOWERCASE);
    }

    public static String normalizePhoneNumber(String phone) {
        if (phone == null) {
            return null;
        }
        return phone.trim().replaceAll("[^0-9+]", "");
    }

    public static String normalizeName(String name) {
        if (name == null || name.isBlank()) {
            return name;
        }

        String collapsed = normalize(name, Normalize.NormalizeType.TRIM, Normalize.NormalizeType.COLLAPSE_WHITESPACE);

        String[] words = collapsed.split(" ");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            if (i > 0) {
                result.append(" ");
            }
            String word = words[i];
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)));
                if (word.length() > 1) {
                    result.append(word.substring(1).toLowerCase());
                }
            }
        }

        return result.toString();
    }
}

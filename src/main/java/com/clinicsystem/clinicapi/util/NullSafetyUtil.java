package com.clinicsystem.clinicapi.util;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * Utility class for null-safe operations
 */
public final class NullSafetyUtil {

    private NullSafetyUtil() {
        throw new AssertionError("Utility class cannot be instantiated");
    }

    /**
     * Returns the value if not null, otherwise returns the default value
     */
    public static <T> T getOrDefault(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    /**
     * Returns the value if not null, otherwise returns the result of the supplier
     */
    public static <T> T getOrDefault(T value, Supplier<T> defaultSupplier) {
        return value != null ? value : defaultSupplier.get();
    }

    /**
     * Check if string is null or empty
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Check if string is not null and not empty
     */
    public static boolean isNotNullOrEmpty(String str) {
        return !isNullOrEmpty(str);
    }

    /**
     * Get string or empty if null
     */
    public static String getOrEmpty(String str) {
        return str != null ? str : "";
    }

    /**
     * Get string or default if null/empty
     */
    public static String getOrDefault(String str, String defaultValue) {
        return isNullOrEmpty(str) ? defaultValue : str;
    }

    /**
     * Check if collection is null or empty
     */
    public static boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Check if collection is not null and not empty
     */
    public static boolean isNotNullOrEmpty(Collection<?> collection) {
        return !isNullOrEmpty(collection);
    }

    /**
     * Require non-null value, throw IllegalArgumentException if null
     */
    public static <T> T requireNonNull(T value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
        return value;
    }

    /**
     * Require non-null and non-empty string, throw IllegalArgumentException if null
     * or empty
     */
    public static String requireNonEmpty(String value, String fieldName) {
        if (isNullOrEmpty(value)) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
        return value;
    }
}

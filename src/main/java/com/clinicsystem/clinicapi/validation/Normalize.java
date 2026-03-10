package com.clinicsystem.clinicapi.validation;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.lang.annotation.*;

@Documented
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@JsonDeserialize(using = NormalizeDeserializer.class)
public @interface Normalize {

    NormalizeType[] value() default { NormalizeType.TRIM };

    enum NormalizeType {
        TRIM,
        LOWERCASE,
        UPPERCASE,
        COLLAPSE_WHITESPACE,
        REMOVE_ALL_WHITESPACE,
        REMOVE_SPECIAL_CHARS
    }
}

package com.clinicsystem.clinicapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchCriteria {
    private String key; // Field name
    private String operation; // eq, ne, gt, lt, gte, lte, like, in
    private Object value; // Search value

    public enum Operation {
        EQUALS("eq"),
        NOT_EQUALS("ne"),
        GREATER_THAN("gt"),
        LESS_THAN("lt"),
        GREATER_THAN_OR_EQUALS("gte"),
        LESS_THAN_OR_EQUALS("lte"),
        LIKE("like"),
        IN("in"),
        BETWEEN("between");

        private final String value;

        Operation(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Operation fromValue(String value) {
            for (Operation op : values()) {
                if (op.value.equalsIgnoreCase(value)) {
                    return op;
                }
            }
            throw new IllegalArgumentException("Unknown operation: " + value);
        }
    }
}
